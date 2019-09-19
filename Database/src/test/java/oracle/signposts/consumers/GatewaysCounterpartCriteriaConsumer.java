package oracle.signposts.consumers;

import oracle.signposts.gateways.GatewayFeatureType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class GatewaysCounterpartCriteriaConsumer implements ICriteriaConsumer {
    private static final int DEFAULT_FETCH_SIZE = 1_000;

    private Map<Integer, AtomicInteger> gatewaysCount = new HashMap<>();
    private Map<GatewayFeatureType, Set<Integer>> gatewaysPerType = new EnumMap<>(GatewayFeatureType.class);
    private Map<String, Set<Integer>> gatewaysPerUR = new HashMap<>();

    @Override
    public void processDbUser(String dbUser, String dbServerURL) {
        try (Connection connection = DriverManager.getConnection(dbServerURL, dbUser, "password")) {
            processDbUser(connection, dbUser, dbServerURL);
        } catch (SQLException e) {
            System.out.println(String.format("Unable to process dbUser = %s, dbServerURL = %s. Cause:%n%s", dbUser, dbServerURL, e));
        }
    }

    @Override
    public void processDbUser(Connection connection, String dbUser, String dbServerURL) {
        for (GatewayFeatureType gatewayType : GatewayFeatureType.values()) {
            try (ResultSet resultSet = connection.createStatement().executeQuery(gatewayType.getQuery(dbUser))) {
                resultSet.setFetchSize(DEFAULT_FETCH_SIZE);
                while (resultSet.next()) {
                    int gatewayId = resultSet.getInt(1);
                    gatewaysCount.computeIfAbsent(gatewayId, value -> new AtomicInteger()).incrementAndGet();
//                    gatewaysPerType.computeIfAbsent(gatewayType, value -> new HashSet<>()).add(gatewayId);
                    gatewaysPerUR.computeIfAbsent(dbUser, value -> new HashSet<>()).add(gatewayId);
                }
            } catch (SQLException e) {
                System.err.println(String.format("Unable to process dbUser = %s, dbServerURL = %s, query = %s. Cause:%n%s",
                        dbUser, dbServerURL, gatewayType.getQuery(dbUser), e));
            }
        }
    }

    public void printGatewaysWithoutCounterPart() {
//        Map<GatewayFeatureType, Set<Integer>> singleGateways = new EnumMap<>(GatewayFeatureType.class);
        Map<String, Set<Integer>> singleGateways = new HashMap<>();
        gatewaysCount.entrySet().stream()
                .filter(e -> e.getValue().get() < 2)
                .map(Map.Entry::getKey)
                .forEach(gatewayId -> gatewaysPerUR.entrySet().stream()
                        .filter(e -> e.getValue().contains(gatewayId))
//                        .forEach(e -> singleGateways.computeIfAbsent(e.getKey(), value -> new HashSet<>()).add(gatewayId)));
                        .forEach(e -> singleGateways.computeIfAbsent(e.getKey(), value -> new HashSet<>()).add(gatewayId)));
        singleGateways.forEach((type, gatewayIDs) ->
//                System.out.println(String.format("Gateways = %s have no counterpart, feature type = %s", gatewayIDs, type)));
                System.out.println(String.format("Gateways = %s have no counterpart, update region = %s", gatewayIDs, type)));
    }
}
