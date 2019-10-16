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
import java.util.stream.Collectors;

public class SpeedProfilesCriteriaConsumer implements ICriteriaConsumer {
    private static final String QUERY = "select distinct SPEED_KPH from NTP_SPEED_PATTERN";

    private final String market;
    private final String dvn;
    private Set<Integer> speeds = new TreeSet<>();
    private Map<String, Set<Integer>> speedPerRegion = new TreeMap<>();

    public SpeedProfilesCriteriaConsumer(String market, String dvn) {
        this.market = market;
        this.dvn = dvn;
    }

    @Override
    public void processDbUser(String dbUser, String dbServerURL) {

    }

    @Override
    public void processDbUser(Connection connection, String dbUser, String dbServerURL) {
        String query = ICriteria.getQuery(QUERY, dbUser);
        try (ResultSet resultSet = connection.createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                int speed = resultSet.getInt(1);
                speedPerRegion.computeIfAbsent(dbUser, regions -> new TreeSet<>()).add(speed);
                speeds.add(speed);
            }
        } catch (SQLException e) {
            System.err.println(String.format("Unable to process dbUser = %s, dbServerURL = %s, query = %s. Cause:%n%s",
                    dbUser, dbServerURL, query, e));
        }
    }

    public void printSpeedProfiles()
    {
        speedPerRegion.forEach((region, speeds) ->
        {
            StringBuilder builder = new StringBuilder();
            builder.append('|').append(UserReader.convertDbUserToRegion(region, dvn)).append('|')
                    .append(speeds.stream().map(Object::toString).collect(Collectors.joining(","))).append('|')
                    .append(speeds.size()).append('|');
            System.out.println(builder.toString());
        });
        StringBuilder builder = new StringBuilder();
        builder.append('|').append(market).append('|')
                .append(speeds.stream().map(Object::toString).collect(Collectors.joining(","))).append('|')
                .append(speeds.size()).append('|');
        System.out.println(builder.toString());
    }
}
