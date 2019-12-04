package oracle.checker.consumers;

import oracle.checker.QueryTimeouter;
import oracle.checker.gateways.GatewayFeatureType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

public class GatewaysCounterpartCriteriaConsumer implements ICriteriaConsumer {
    private String market;

    private Map<Integer, AtomicInteger> gatewaysCount = new HashMap<>();
    private Map<GatewayFeatureType, Set<Integer>> gatewaysPerType = new EnumMap<>(GatewayFeatureType.class);
    private Map<String, Set<Integer>> gatewaysPerUR = new HashMap<>();
    // by regions
    private Map<Integer, Set<String>> gatewayIdToRegions = new HashMap<>();

    public GatewaysCounterpartCriteriaConsumer(String market) {
        this.market = market;
    }

    @Override
    public void processDbUser(String dbUser, String dbServerURL) throws Exception {
        try (Connection connection = DriverManager.getConnection(dbServerURL, dbUser, "password")) {
            processDbUser(connection, dbUser, dbServerURL);
        }
    }

    @Override
    public void processDbUser(Connection connection, String dbUser, String dbServerURL) throws Exception {
        for (GatewayFeatureType gatewayType : GatewayFeatureType.values()) {
//            try (ResultSet resultSet = connection.createStatement().executeQuery(gatewayType.getQuery(dbUser))) {
            try (ResultSet resultSet = QueryTimeouter.getResultSet(connection, gatewayType.getQuery(dbUser))) {
                resultSet.setFetchSize(DEFAULT_FETCH_SIZE);
                while (resultSet.next()) {
                    int gatewayId = resultSet.getInt(1);
                    gatewaysCount.computeIfAbsent(gatewayId, value -> new AtomicInteger()).incrementAndGet();
//                    gatewaysPerType.computeIfAbsent(gatewayType, value -> new HashSet<>()).add(gatewayId);
                    gatewaysPerUR.computeIfAbsent(dbUser, value -> new HashSet<>()).add(gatewayId);

                    gatewayIdToRegions.computeIfAbsent(gatewayId, value -> new HashSet<>()).add(dbUser);
                }
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

    public void printGatewaysForRegion(String dbUser)
    {
        System.out.println(String.format("Show all gateways for %s and it neighbours", dbUser));
        gatewayIdToRegions.entrySet().stream()
                .filter(e -> e.getValue().contains(dbUser))
                .forEach(e -> System.out.println(String.format("GatewayId = %d => %s",  e.getKey(), e.getValue())));
    }

    public void printGatewaysForRegion(String market, String dbUser, Set<Integer> targetGatewayIDs)
    {
        if (this.market.equals(market))
        {
            System.out.println(String.format("Show all gateways for %s and it neighbours", dbUser));
            gatewayIdToRegions.entrySet().stream()
                    .filter(e -> targetGatewayIDs.contains(e.getKey()))
                    .filter(e -> e.getValue().contains(dbUser))
                    .forEach(e -> System.out.println(String.format("GatewayId = %d => %s",  e.getKey(), e.getValue())));
        }
    }

    public void printNeighbours()
    {
        Map<String, Set<String>> neighbourRegions = new TreeMap<>();
        gatewaysPerUR.forEach((region, gateways) ->
        {
            gateways.forEach(gatewayId ->
            {
                Set<String> neighbours = gatewayIdToRegions.getOrDefault(gatewayId, Collections.emptySet());
                neighbourRegions.computeIfAbsent(region, value -> new TreeSet<>()).addAll(neighbours);
            });
            neighbourRegions.get(region).remove(region);
        });
        System.out.println("Region with neighbours: " + neighbourRegions);
    }
}
