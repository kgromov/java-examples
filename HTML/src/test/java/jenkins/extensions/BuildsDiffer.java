package jenkins.extensions;


import com.google.common.collect.ImmutableMap;
import jenkins.Settings;
import jenkins.forkjoinpool.BuildInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import static jenkins.extensions.Convertor.BUILD_START_DATE;

public class BuildsDiffer {
    private static final String DB_URI_PREFIX = "jdbc:sqlite:file:";
    private static final String ATTACH_QUERY = "ATTACH database '%s' as new";
    private static final String ALTER_QUERY = "ALTER TABLE main.%s ADD %s INT (6)";
    private static final String DETACH_QUERY = "DETACH database new";

    private static final ToIntFunction<Path> TO_BUILD_NUMBER = path ->
            Integer.parseInt(path.getFileName().toString().replaceAll(".sq3", "").replaceAll("[^\\d]", ""));

    private Path outputFilePath;
    // relevant for standalone GUI application
    private String jobType;

    public BuildsDiffer() {
        this.outputFilePath = getOutputFilePath(Settings.getInstance());
    }

    private Path getOutputFilePath(Settings settings) {
        try {
            Path outputFolder = Paths.get(settings.getDbOutputDir(), "trend", settings.getJobName(), settings.getDvn());
            Files.createDirectories(outputFolder);
            return outputFolder.resolve(settings.getJobName() + ".sq3");
        } catch (IOException e) {
            throw new RuntimeException("Unable to create build time trend folder: " + e.getMessage());
        }
    }

    public void collectBuildTimeTrend() {
        try {
            Settings settings = Settings.getInstance();
            Path outputFolder = Paths.get(settings.getDbOutputDir(), "trend", settings.getJobName(), settings.getDvn());
            Files.createDirectories(outputFolder);
            List<Path> buildFiles = Files.find(Paths.get(settings.getDbOutputDir()),
                    Short.MAX_VALUE,
                    (filePath, fileAttr) -> fileAttr.isRegularFile() && filePath.toString().contains(settings.getJobName())
                            && filePath.toString().contains(settings.getDvn())
                            && filePath.toString().contains("connections"))
                    .sorted(Comparator.comparingInt(TO_BUILD_NUMBER))
                    .collect(Collectors.toList());
            Path outputFilePath = outputFolder.resolve(settings.getJobName() + ".sq3");
            if (buildFiles.isEmpty()) {
                return;
            }
            Files.deleteIfExists(outputFilePath);
            Files.createFile(outputFilePath);
            // init trend - create table with 1st build
            createTrend(buildFiles.get(0));
            // append rest builds to existed tables
            appendBuildsToTrend(buildFiles.subList(1, buildFiles.size()).toArray(new Path[buildFiles.size() - 1]));
        } catch (IOException e) {
            System.err.println("Unable to create build time trend folder: " + e.getMessage());
        }
    }

    // ============= Create =============
    private static final String CREATE_REGIONS_TREND = "CREATE TABLE UPDATE_REGIONS_TREND AS\n" +
            "select SUBSTR(jobName, instr(jobName, '-') + 1) as UpdateRegion, BuildTime AS %s from RunSummary\n" +
            "WHERE DisplayName  = 'Compile'\n" +
            "order by UpdateRegion";

    private static final String CREATE_PRODUCT_TREND = "CREATE TABLE PRODUCTS_TREND AS\n" +
            "select SUBSTR(SUBSTR(jobName, instr(jobName, '-') + 1), 0, instr(SUBSTR(jobName, instr(jobName, '-') + 1), '_')) as product, " +
            "sum(BuildTime) as %s from RunSummary\n" +
            "WHERE DisplayName  = 'Compile'\n" +
            "group by product\n" +
            "order by product";

    private void createTrend(Path buildPath) {
        try (Connection connection = DriverManager.getConnection(DB_URI_PREFIX + outputFilePath.toString());
             Statement statement = connection.createStatement()) {
            int buildNumber = TO_BUILD_NUMBER.applyAsInt(buildPath);
            String buildColumn = String.format("'Build#%d'", buildNumber);
            statement.execute(String.format(ATTACH_QUERY, buildPath));

            statement.execute(String.format(CREATE_REGIONS_TREND, buildColumn));
            statement.execute(String.format(CREATE_PRODUCT_TREND, buildColumn));

            statement.execute(DETACH_QUERY);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ============= Append =============
    private static final String INSERT_QUERY_UPDATE_REGIONS = "with new_build as\n" +
            "(\n" +
            "   select SUBSTR(jobName, instr(jobName, '-') +  1) as UpdateRegion, BuildTime from new.RunSummary \n" +
            "            WHERE DisplayName  = 'Compile' \n" +
            "            order by UpdateRegion\n" +
            ")\n" +
            "UPDATE main.UPDATE_REGIONS_TREND set %s =\n" +
            "(\n" +
            "            select BuildTime from new_build\n" +
            "            WHERE main.UPDATE_REGIONS_TREND.UpdateRegion  = new_build.UpdateRegion \n" +
            ")";

    private static final String INSERT_QUERY_PRODUCTS = "with new_build as \n" +
            "(\n" +
            "    select SUBSTR(SUBSTR(jobName, instr(jobName, '-') + 1), 0, instr(SUBSTR(jobName, instr(jobName, '-') + 1), '_')) as product,  " +
            "    sum(BuildTime) as BuildTime from new.RunSummary\n" +
            "            WHERE DisplayName  = 'Compile'\n" +
            "            group by product\n" +
            "            order by product\n" +
            ")\n" +
            "UPDATE main.PRODUCTS_TREND set %s =\n" +
            "(\n" +
            "            select BuildTime from new_build\n" +
            "            WHERE main.PRODUCTS_TREND.product  = new_build.product \n" +
            ")";

    private static final Map<String, String> INSERT_QUERIES = ImmutableMap.<String, String>builder()
            .put("UPDATE_REGIONS_TREND", INSERT_QUERY_UPDATE_REGIONS)
            .put("PRODUCTS_TREND", INSERT_QUERY_PRODUCTS)
            .build();

    public void appendToTrend(BuildInfo upstreamBuild)
    {
        Settings settings = Settings.getInstance();
        // connections\<jobName>\<DVN>\<build_timestamp>\<jobName>-<buildNumber>.sq3
        Path inputFolder = Paths.get(settings.getDbOutputDir(),
                "connections", settings.getJobName(), settings.getDvn(), BUILD_START_DATE.format(new Date(upstreamBuild.getTimestamp())));
        Path buildPath = inputFolder.resolve(settings.getJobName() + "-" + settings.getBuildNumber() + ".sq3");
        appendBuildsToTrend(buildPath);
    }

    private void appendBuildsToTrend(Path... buildPaths) {
        try (Connection connection = DriverManager.getConnection(DB_URI_PREFIX + outputFilePath.toString());
             Statement statement = connection.createStatement()) {
            for (Path buildPath : buildPaths) {
                int buildNumber = TO_BUILD_NUMBER.applyAsInt(buildPath);
                String buildColumn = String.format("'Build#%d'", buildNumber);
                // attach
                statement.execute(String.format(ATTACH_QUERY, buildPath));
                for (Map.Entry<String, String> entry : INSERT_QUERIES.entrySet()) {
                    String tableName = entry.getKey();
                    String query = entry.getValue();
                    // alter
                    statement.execute(String.format(ALTER_QUERY, tableName, buildColumn));
                    // insert
                    statement.execute(String.format(query, buildColumn));
                }
                // detach
                statement.execute(DETACH_QUERY);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
       /* String pathToFirstBuild = "C:\\HERE-CARDS\\products_per_dvn\\191F0\\connections\\31-08-2019\\NPB-148-NAR-FULL.sq3";
        String pathToSecondBuild = "C:\\HERE-CARDS\\products_per_dvn\\191F0\\connections\\04-09-2019\\NPB-150-NAR-FULL.sq3";*/
        new BuildsDiffer().collectBuildTimeTrend();
    }

}
