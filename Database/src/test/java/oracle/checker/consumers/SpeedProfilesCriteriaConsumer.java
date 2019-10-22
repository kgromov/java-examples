package oracle.checker.consumers;

import com.google.common.collect.ImmutableMap;
import lombok.Data;
import lombok.Getter;
import oracle.checker.criterias.ICriteria;
import oracle.speed_profiles.SpeedProfile;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SpeedProfilesCriteriaConsumer implements ICriteriaConsumer {
    private static final String DB_URI_PREFIX = "jdbc:sqlite:file:";
        private static final String TABLE_NAME = "NTTP_SPEED_PATTERN";

    private static final String TEXT_TYPE = "TEXT";
    private static final String INTEGER_TYPE = "INTEGER";

    private static final Map<String, String> COLUMN_TYPE_BY_COLUMN_NAME = ImmutableMap.<String, String>builder()
            .put("PATTERN_ID", INTEGER_TYPE)
            .put("SEQ_NUM", INTEGER_TYPE)
            .put("SAMPLING_ID", INTEGER_TYPE)
            .put("START_TIME", TEXT_TYPE)
            .put("END_TIME", TEXT_TYPE)
            .put("SPEED_KPH", INTEGER_TYPE)
            .build();

    private static final String QUERY = "select distinct SAMPLING_ID, PATTERN_ID from NTP_SPEED_PATTERN";
    private static final String FULL_ROW_QUERY = "select PATTERN_ID, SEQ_NUM, SAMPLING_ID, SPEED_KPH, " +
            "to_char(START_TIME, 'hh24:mi') as START_TIME, " +
            "to_char(END_TIME, 'hh24:mi') as END_TIME from NTP_SPEED_PATTERN " +
            "order by PATTERN_ID, SEQ_NUM";

    private final String market;
    private final String dvn;
    private Map<String, Map<Integer, Set<Integer>>> profileIdsPerRegion = new TreeMap<>();
    private Map<Integer, Set<Integer>> profileIdsPerMarket = new TreeMap<>();
    // probably add SAMPLING_ID as well
    private Set<SpeedProfileRow> speedProfileRows = new TreeSet<>(Comparator.comparingInt(SpeedProfileRow::getPatternId)
            .thenComparing(SpeedProfileRow::getSeqNum));

    private final Comparator<Pair<Integer, Integer>> comparator = (o1, o2) -> Integer.compareUnsigned(
            Integer.compareUnsigned(o1.getKey(), o2.getKey()),
            Integer.compareUnsigned(o1.getValue(), o2.getValue())
            );
    @Getter
    private Map<Pair<Integer, Integer>, SpeedProfile> profiles = new TreeMap<>(comparator);

    public SpeedProfilesCriteriaConsumer(String market, String dvn) {
        this.market = market;
        this.dvn = dvn;
    }

    @Override
    public void processDbUser(String dbUser, String dbServerURL) {

    }

    @Override
    public void processDbUser(Connection connection, String dbUser, String dbServerURL) {
        long start = System.nanoTime();
        String query = ICriteria.getQuery(FULL_ROW_QUERY, dbUser);
        try (ResultSet resultSet = connection.createStatement().executeQuery(query)) {
            resultSet.setFetchSize(DEFAULT_FETCH_SIZE * DEFAULT_FETCH_SIZE);
            while (resultSet.next()) {
                int samplingId = resultSet.getInt("SAMPLING_ID");
                int patternId = resultSet.getInt("PATTERN_ID");
                int seqNum = resultSet.getInt("SEQ_NUM");
                int speed = resultSet.getInt("SPEED_KPH");
                String startTime = resultSet.getString("START_TIME");
                String endTime = resultSet.getString("END_TIME");

                /*SpeedProfileRow profileRow = new SpeedProfileRow(patternId, seqNum, samplingId, startTime, endTime, speed);
                speedProfileRows.add(profileRow);
*/
                profiles.computeIfAbsent(Pair.of(samplingId, patternId),
                        profile -> new SpeedProfile(patternId, samplingId)).addSpeed(speed);

                profileIdsPerRegion.computeIfAbsent(dbUser, samplings -> new TreeMap<>())
                        .computeIfAbsent(samplingId, patterns -> new TreeSet<>()).add(patternId);
                profileIdsPerMarket.computeIfAbsent(samplingId, patterns -> new TreeSet<>()).add(patternId);
            }
        } catch (SQLException e) {
            LOGGER.error(String.format("Unable to process dbUser = %s, dbServerURL = %s, query = %s. Cause:%n%s",
                    dbUser, dbServerURL, query, e));
        }
        finally {
            LOGGER.trace(String.format("#processDbUser: Time elapsed = %d ms",
                    TimeUnit.MILLISECONDS.convert(System.nanoTime()- start, TimeUnit.NANOSECONDS)));
        }
    }

    public void printSpeedProfiles()
    {
        LOGGER.info("Per Regions:");
        profileIdsPerRegion.forEach((region, patterns) ->
        {
            StringBuilder builder = new StringBuilder();
            builder.append('|').append(region).append('|');
            patterns.forEach((sampleId, patternIds) ->
                    builder.append(patternIds.size()).append('|')
            );
            System.out.println(builder.toString());
        });
        StringBuilder builder = new StringBuilder("|Total|");
        profileIdsPerMarket.forEach((sampleId, patternIds) -> builder.append(patternIds.size()).append('|'));
        System.out.println("Per Market:\n" + builder.toString());

      /*  profileIdsPerRegion.forEach((region, speeds) ->
        {
            StringBuilder builder = new StringBuilder();
            builder.append('|').append(UserReader.convertDbUserToRegion(region, dvn)).append('|')
                    .append(speeds.stream().map(Object::toString).collect(Collectors.joining(","))).append('|')
                    .append(speeds.size()).append('|');
            System.out.println(builder.toString());
        });
        StringBuilder builder = new StringBuilder();
        builder.append('|').append(market).append('|')
                .append(profileIds.stream().map(Object::toString).collect(Collectors.joining(","))).append('|')
                .append(profileIds.size()).append('|');
        System.out.println(builder.toString());*/
    }

    public void exportToSq3() {
        Path outputFile = Paths.get("Database", "src", "test", "java", "oracle", "output", "NTP_SPEED_PROFILES_"+ market + ".sq3");
        try
        {
            Files.deleteIfExists(outputFile);
            Files.createFile(outputFile);
            // register sqlite driver
            Class.forName("org.sqlite.JDBC");
            try  (Connection connection = DriverManager.getConnection(DB_URI_PREFIX + outputFile.toString())){
                createTable(connection);
                writeToSqLite(connection, speedProfileRows);
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException("Export to sqlite failed", e);
        }
    }

    private static String getCreateQuery() {
        return "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME +
                COLUMN_TYPE_BY_COLUMN_NAME.entrySet().stream()
                        .map(columnData -> columnData.getKey() + " " + columnData.getValue())
                        .collect(Collectors.joining(" NOT NULL, ", " (", " NOT NULL)"));
    }

    private static String getInsertQuery() {
        return "INSERT INTO " +
                TABLE_NAME +
                COLUMN_TYPE_BY_COLUMN_NAME.keySet().stream()
                        .collect(Collectors.joining(",", " (", ") ")) +
                COLUMN_TYPE_BY_COLUMN_NAME.entrySet().stream()
                        .map(columnData -> "?")
                        .collect(Collectors.joining(",", "VALUES (", ") "));
    }

    private void createTable(Connection connection) throws SQLException {
        try  (PreparedStatement statement = connection.prepareStatement(getCreateQuery())) {
            statement.execute();
        }
    }

    private void writeToSqLite(Connection connection, Set<SpeedProfileRow> speedProfileRows) throws SQLException {
        try  (  PreparedStatement statement = connection.prepareStatement(getInsertQuery())){
            connection.setAutoCommit(false);
            for (SpeedProfileRow row : speedProfileRows)
            {
                int columnIndex = 0;
                statement.setInt(++columnIndex, row.getPatternId());
                statement.setInt(++columnIndex, row.getSeqNum());
                statement.setInt(++columnIndex, row.getSamplingId());
                statement.setString(++columnIndex, row.getStartTime());
                statement.setString(++columnIndex, row.getEndTime());
                statement.setInt(++columnIndex, row.getSpeed());
                statement.addBatch();
            }
            statement.executeBatch();
            connection.commit();
        }
    }

    @Data
    private static final class SpeedProfileRow
    {
        private final int patternId;
        private final int seqNum;
        private final int samplingId;
        private final String startTime;
        private final String endTime;
        private final int speed;
    }
}
