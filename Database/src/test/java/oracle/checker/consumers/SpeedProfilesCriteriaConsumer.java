package oracle.checker.consumers;

import oracle.checker.criterias.ICriteria;
import oracle.checker.readers.UserReader;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SpeedProfilesCriteriaConsumer implements ICriteriaConsumer {
    private static final String QUERY = "select distinct SAMPLING_ID, PATTERN_ID from NTP_SPEED_PATTERN";

    private final String market;
    private final String dvn;
    private Map<String, Map<Integer, Set<Integer>>> profileIdsPerRegion = new TreeMap<>();
    private Map<Integer, Set<Integer>> profileIdsPerMarket = new TreeMap<>();

    public SpeedProfilesCriteriaConsumer(String market, String dvn) {
        this.market = market;
        this.dvn = dvn;
    }

    @Override
    public void processDbUser(String dbUser, String dbServerURL) {

    }

    @Override
    public void processDbUser(Connection connection, String dbUser, String dbServerURL) {
        long startTime = System.nanoTime();
        String query = ICriteria.getQuery(QUERY, dbUser);
        try (ResultSet resultSet = connection.createStatement().executeQuery(query)) {
            resultSet.setFetchSize(DEFAULT_FETCH_SIZE);
            while (resultSet.next()) {
                int samplingId = resultSet.getInt(1);
                int patternId = resultSet.getInt(2);
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
                    TimeUnit.MILLISECONDS.convert(System.nanoTime()- startTime, TimeUnit.NANOSECONDS)));
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
}
