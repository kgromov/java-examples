package jenkins;


import com.google.common.collect.ImmutableMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * SELECT DISTINCT SUBSTR(eu177.KeepUser, 0, instr(eu177.KeepUser, '_10_126')) as KeepUser, eu177.BuildTime as BuildTime_177,  eu179.BuildTime as BuildTime_179
 * FROM main.RunSummary eu177
 * JOIN NEW.RunSummary  eu179 on SUBSTR(eu177.KeepUser, 0, instr(eu177.KeepUser, '_10_126')) = SUBSTR(eu179.KeepUser, 0, instr(eu179.KeepUser, '_10_126'))
 * WHERE eu177.DisplayName  = 'Compile' and BuildTime_179 > BuildTime_177
 */
public class BuildsDiffer {
    private static final String DB_URI_PREFIX = "jdbc:sqlite:file:";
    private static final String ATTACH_QUERY = "ATTACH database '%s' as %s";
    private static final String ALTER_QUERY = "ALTER TABLE new.%s ADD %s INT (6)";
    private static final String INSERT_QUERY_UPDATE_REGIONS = "INSERT INTO main.UPDATE_REGIONS_TREND (%s) " +
            "SELECT b2.BuildTime " +
            "FROM main.RunSummary b1 " +
            "LEFT JOIN NEW.RunSummary b2 on SUBSTR(b1.jobName, instr(b1.jobName, '-') + 1)  = SUBSTR(b2.jobName, instr(b2.jobName, '-') + 1) "+
            "WHERE b1.DisplayName  = 'Compile'";

    private static final String INSERT_QUERY_PRODUCTS = "INSERT INTO main.PRODUCTS_TREND (%s) " +
            "SELECT sum(BuildTime) FROM new.RunSummary " +
            "WHERE DisplayName  = 'Compile' " +
            "GROUP by SUBSTR(b1.KeepUser, 0, instr(b1.KeepUser, '_')) "+
            "ORDER BY SUBSTR(b1.KeepUser, 0, instr(b1.KeepUser, '_'))";

    private static final String DETACH_QUERY = "DETACH database %s";
    private static final String PREV_CATALOG = "prev";
    private static final String NEW_CATALOG = "new";

    /*
    SELECT substr(UpdateRegion, 0, instr(UpdateRegion, '_')) as product, sum(BuildTime_177) as BuildTime_Total_158, sum(BuildTime_179) as BuildTime_Total_160
    from buidTrendPerUpdateRegion
    GROUP by product
     */

    private static final String REGION_CREATE_QUERY = "CREATE TABLE UPDATE_REGIONS_TREND AS " +
            "SELECT DISTINCT SUBSTR(b1.KeepUser, 0, instr(b1.KeepUser, '_10_126')) as UpdateRegion, " +
            "b1.BuildTime as BuildTime_1,  b2.BuildTime as BuildTime_2 " +
            "FROM prev.RunSummary b1 " +
            "JOIN new.RunSummary  b2 on SUBSTR(b1.KeepUser, 0, instr(b1.KeepUser, '_10_126')) = SUBSTR(b2.KeepUser, 0, instr(b2.KeepUser, '_10_126')) " +
            "WHERE b1.DisplayName  = 'Compile'";

    private static final String PRODUCT_CREATE_QUERY = "CREATE TABLE PRODUCTS_TREND AS " +
            "SELECT SUBSTR(b1.KeepUser, 0, instr(b1.KeepUser, '_')) as product, " +
            "sum(b1.BuildTime) as BuildTime_Total_1, sum(b2.BuildTime) as BuildTime_Total_2 " +
            "FROM prev.RunSummary b1 " +
            "JOIN new.RunSummary  b2 on SUBSTR(b1.KeepUser, 0, instr(b1.KeepUser, '_10_126')) = SUBSTR(b2.KeepUser, 0, instr(b2.KeepUser, '_10_126')) " +
            "WHERE b1.DisplayName  = 'Compile' " +
            "GROUP by product";

    private Map<String, String> headers = ImmutableMap.<String, String>builder()
            .put("UPDATE_REGIONS_TREND", "SELECT DISTINCT SUBSTR(b1.KeepUser, 0, instr(b1.KeepUser, '_10_126')) as UpdateRegion")
            .put("PRODUCTS_TREND", "SELECT SUBSTR(b1.KeepUser, 0, instr(b1.KeepUser, '_')) as product")
            .build();

    private static final Map<String, String> INSERT_QUERIES = ImmutableMap.<String, String>builder()
            .put("UPDATE_REGIONS_TREND", INSERT_QUERY_UPDATE_REGIONS)
            .put("PRODUCTS_TREND", INSERT_QUERY_PRODUCTS)
            .build();

    private String folder;
    // TODO: implement enum later on {NPB-FULL, NPB-EXTENDED, PreSubmit, Validation_Suite_Pre_Submit}
    private String jobType;

    public BuildsDiffer(String jobType) {
        this.jobType = jobType;
    }

    // TODO: create table for the first build
    public void collectBuildTimeTrend(Settings settings) {
        try {
            Path outputFolder = Paths.get(settings.getDbOutputDir(), "trend", settings.getJobName(), settings.getDvn());
            Files.createDirectories(outputFolder);
            List<Path> buildFiles = Files.find(Paths.get(settings.getDbOutputDir()),
                    Short.MAX_VALUE,
                    (filePath, fileAttr) -> fileAttr.isRegularFile() && filePath.toString().contains(settings.getJobName())
                            && filePath.toString().contains("connections"))
                    .collect(Collectors.toList());
            Path outputFilePath = outputFolder.resolve(settings.getJobName() + ".sq3");
            Files.deleteIfExists(outputFilePath);
            Files.createFile(outputFilePath);
            writeRegionStat(outputFilePath, buildFiles, "UPDATE_REGIONS_TREND");
        } catch (IOException e) {
            System.err.println("Unable to create build time trend folder: " + e.getMessage());
        }
    }

    private String buildCreateTableQuery(String tableName, List<Path> buildFiles) {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE ").append(tableName).append(" AS");

        query.append("SELECT DISTINCT SUBSTR(b1.KeepUser, 0, instr(b1.KeepUser, '_10_126')) as UpdateRegion, ");
        query.append("WHERE b1.DisplayName  = 'Compile'");
        return query.toString();
    }


    private void writeRegionStat(Path buildTimeTrendDbPath, List<Path> buildPaths, String tableName) {
        try (Connection connection = DriverManager.getConnection(DB_URI_PREFIX + buildTimeTrendDbPath.toString());
             Statement statement = connection.createStatement()) {
            for (Path buildPath : buildPaths) {
                int buildNumber = Integer.parseInt(buildPath.getFileName().toString().replaceAll(".sq3", "")
                        .replaceAll("[^\\d]", ""));
                String buildColumn = String.format("'Build#%d'", buildNumber);
                // attach
                statement.execute(String.format(ATTACH_QUERY, buildPath, NEW_CATALOG));
                // alter
                statement.execute(String.format(ALTER_QUERY, tableName, buildColumn));
                // insert
                statement.execute(String.format(INSERT_QUERIES.get(tableName), buildColumn));
                // detach
                statement.execute(String.format(DETACH_QUERY, NEW_CATALOG));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // NPB-160-EU-FULL.sq3
    public void compareBuilds(String pathToFirstBuild, String pathToSecondBuild) {
        Path path1 = Paths.get(pathToFirstBuild);
        Path path2 = Paths.get(pathToSecondBuild);
        int buildNumber1 = Integer.parseInt(path1.getFileName().toString().replaceAll(".sq3", "")
                .replaceAll("[^\\d]", ""));
        int buildNumber2 = Integer.parseInt(path2.getFileName().toString().replaceAll(".sq3", "")
                .replaceAll("[^\\d]", ""));
        // TODO: probably buildNUmber comparison to get max (new) and min (old)
        Path resultFile = Paths.get("C:\\HERE-CARDS\\products_per_dvn\\diff",
                jobType + "_" + buildNumber1 + "_vs_" + buildNumber2 + ".sq3");
        writeRegionStat(pathToFirstBuild, pathToSecondBuild, resultFile.toString());
    }

    private void writeRegionStat(String pathToFirstBuild, String pathToSecondBuild, String resultFile) {
        try (Connection connection = DriverManager.getConnection(DB_URI_PREFIX + resultFile);
             Statement statement = connection.createStatement()) {
            // extra attach as create in another db
            statement.execute(String.format(ATTACH_QUERY, pathToFirstBuild, PREV_CATALOG));
            statement.execute(String.format(ATTACH_QUERY, pathToSecondBuild, NEW_CATALOG));

            if (statement.execute(REGION_CREATE_QUERY)) {
                System.out.println("UPDATE_REGIONS_TREND table created");
            }

            if (statement.execute(PRODUCT_CREATE_QUERY)) {
                System.out.println("PRODUCTS_TREND table created");
            }

            statement.execute(String.format(DETACH_QUERY, PREV_CATALOG));
            statement.execute(String.format(DETACH_QUERY, NEW_CATALOG));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String pathToFirstBuild = "C:\\HERE-CARDS\\products_per_dvn\\191F0\\connections\\31-08-2019\\NPB-148-NAR-FULL.sq3";
        String pathToSecondBuild = "C:\\HERE-CARDS\\products_per_dvn\\191F0\\connections\\04-09-2019\\NPB-150-NAR-FULL.sq3";
        new BuildsDiffer("NAR_FULL").compareBuilds(pathToFirstBuild, pathToSecondBuild);
    }

}
