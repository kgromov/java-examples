package oracle.speed_profiles;

import dao.CsvDataProvider;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import oracle.checker.consumers.SpeedProfilesCriteriaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataProvider.class.getName());
    private static final String QUERY = "select * from " + SpeedProfilesCriteriaConsumer.TABLE_NAME + " order by PATTERN_ID, SEQ_NUM";
    private static final String OUTPUT_FOLDER = "C:\\Projects\\java-examples\\Database\\src\\test\\java\\oracle\\output";

    public enum Extension
    {
        SQ3 (".sq3"),
        CSV (".csv");

        private String value;

        Extension(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private static void exportSourceDataToCsv() {
        Path outFolder = Paths.get(OUTPUT_FOLDER);
        try {
            Class.forName("org.sqlite.JDBC");
            Files.find(outFolder, 1,
                    ((path, attributes) -> attributes.isRegularFile() && path.toString().endsWith(Extension.SQ3.getValue())))
                    .forEach(dbPath ->
                    {
                        Path csvPath = outFolder.resolve(dbPath.getFileName().toString().replace(Extension.SQ3.getValue(), Extension.CSV.getValue()));
                        try {
                            Files.deleteIfExists(csvPath);
                            Files.createFile(csvPath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try (Connection connection = DriverManager.getConnection(SpeedProfilesCriteriaConsumer.DB_URI_PREFIX + dbPath.toString())) {
                            CsvDataProvider.convertToCsv(connection, SpeedProfilesCriteriaConsumer.TABLE_NAME, csvPath);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<Integer, List<SpeedProfile>> extractSpeedProfiles(String market) {
        long start = System.nanoTime();
        Path outFolder = Paths.get(OUTPUT_FOLDER);
        Path dbPath = outFolder.resolve("NTP_SPEED_PROFILES_" + market + Extension.SQ3.getValue());
        try (Connection connection = DriverManager.getConnection(SpeedProfilesCriteriaConsumer.DB_URI_PREFIX + dbPath.toString());
             ResultSet resultSet = connection.createStatement().executeQuery(QUERY)) {
            Map<Integer, List<SpeedProfile>> speedProfilesPerSamplingId = new HashMap<>();
            Map<Integer, SpeedProfile> speedProfiles = new TreeMap<>();
            while (resultSet.next()) {
                int samplingId = resultSet.getInt("SAMPLING_ID");
                int patternId = resultSet.getInt("PATTERN_ID");
                // TODO: probably speed = speed * 100 for more accurate precision
                int speed = resultSet.getInt("SPEED_KPH");
               /* int seqNum = resultSet.getInt("SEQ_NUM");
                String startTime = resultSet.getString("START_TIME");
                String endTime = resultSet.getString("END_TIME");*/
                SpeedProfile speedProfile = speedProfiles.computeIfAbsent(patternId, profile -> new SpeedProfile(patternId, samplingId));
                // performance optimization - add new created speedProfile
                if (speedProfile.getSpeedPerTime().isEmpty())
                {
                    speedProfilesPerSamplingId.computeIfAbsent(samplingId, profiles -> new ArrayList<>()).add(speedProfile);
                }
                speedProfile.addSpeed(speed);
            }
            return speedProfilesPerSamplingId;
        } catch (SQLException e) {
           throw  new RuntimeException("Unable to extractSpeedProfiles; query = %s" + QUERY, e);
        } finally {
            LOGGER.trace(String.format("#extractSpeedProfiles: Time elapsed = %d ms",
                    TimeUnit.MILLISECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS)));
        }
    }

    public static Path getPath(String fileName, String market, Extension extension)
    {
        Path outFolder = Paths.get(OUTPUT_FOLDER);
        return outFolder.resolve(fileName + "_" + market + extension.getValue());
    }
}
