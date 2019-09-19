package oracle.signposts.gateways;

import com.google.common.collect.ImmutableMap;
import oracle.signposts.UsersReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class CheckUnreachableStubbleLinks {
    private static final String USERS_QUERY = "select username from dba_users WHERE username like 'CDCA_%'";
    private static final String DB_PASSWORD = "password";
    private static final String DB_SERVER_URL = "jdbc:oracle:thin:@akela-%s-%s-0%d.civof2bffmif.us-east-1.rds.amazonaws.com:1521:orcl";
    private static final Map<String, String> MARKET_TO_DVN = ImmutableMap.<String, String>builder()
            .put("eu", "19109")
            .put("nar", "191F0")
            .put("mrm", "191E3")
            .build();

    public static void main(String[] args) {
        long start = System.nanoTime();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            MARKET_TO_DVN.forEach((market, dvn) ->
            {
                System.out.println(String.format("################## market = %s, dvn = %s ##################", market, dvn));
                Set<String> allUsers = new UsersReader(market, dvn).getCdcUsers();
                Set<String> iterateUsers = new HashSet<>(allUsers);
                for (int i = 1; i < 9; i++) {
                    String dbServerUrl = String.format(DB_SERVER_URL, market, dvn, i)
                            // exceptional case
                            .replaceAll("_", "-");
                    System.out.println("Start processing dbServer = " + dbServerUrl);
                    allUsers.stream().filter(iterateUsers::contains).forEach(cdcUser ->
                    {
                        Set<String> dbServerUsers = new HashSet<>();
                        try (Connection connection = DriverManager.getConnection(dbServerUrl, cdcUser, DB_PASSWORD);
                             ResultSet userNames = connection.createStatement().executeQuery(USERS_QUERY)) {
                            while (userNames.next()) {
                                dbServerUsers.add(userNames.getString(1));
//                                dbServerUsers = UsersReader.withoutSampleUsers(dbServerUsers);
                            }
                            iterateUsers.removeAll(dbServerUsers);
                            // users
                            dbServerUsers.parallelStream().forEach(userName -> processRegion(userName, dbServerUrl));
                        } catch (Exception e) {
//                            System.err.println(String.format("No SourceDbUser = %s, dbServer = %s", cdcUser, dbServerUrl));
                        }
                    });
                    System.out.println("Finish processing dbServer = " + dbServerUrl);
                }
                System.out.println("Remaining users: " + iterateUsers);
            });
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            System.out.println(String.format("Time elapsed = %d s", TimeUnit.SECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS)));
        }
    }

    private static final int DEFAULT_FETCH_SIZE = 1000;

    private static final String STUB_NODES_QUERY = "SELECT NODE_ID FROM STUB_NODE";
    private static final String STUB_LINKS_QUERY = "select link_id, ref_node_id, nonref_node_id from STUB_LINK";
    private static final String REAL_LINKS_STUB_NODES_QUERY = "select distinct link_id, ref_node_id as node_id from rdf_link " +
            "where ref_node_id in (select node_id from STUB_NODE) " +
            "union " +
            "select distinct link_id, nonref_node_id as node_id from rdf_link " +
            "where nonref_node_id in (select node_id from STUB_NODE)";
    private static final String UNREACHABLE_CANDIDATES_QUERY = "select distinct sl.link_id, sl.ref_node_id, sl.nonref_node_id from STUB_LINK sl " +
            "join STUB_NAV_LINK snl on sl.LINK_ID = snl.LINK_ID "+
            "where snl.POI_ACCESS = 'Y' " +
                "and sl.ref_node_id in (select node_id from STUB_NODE) " +
                "and sl.nonref_node_id in (select node_id from STUB_NODE)";

    // TODO: sort of BiConsumer
    private static void processRegion(String dbUser, String dbServerURL) {
        try (Connection connection = DriverManager.getConnection(dbServerURL, dbUser, "password");
             ResultSet stubbleNodes = connection.createStatement().executeQuery(STUB_NODES_QUERY);
             ResultSet stubbleLinks = connection.createStatement().executeQuery(STUB_LINKS_QUERY);
             ResultSet realLinks = connection.createStatement().executeQuery(REAL_LINKS_STUB_NODES_QUERY);
             ResultSet unreachableCandidates = connection.createStatement().executeQuery(UNREACHABLE_CANDIDATES_QUERY)) {
            // change fetch size
            stubbleNodes.setFetchSize(DEFAULT_FETCH_SIZE);
            stubbleLinks.setFetchSize(DEFAULT_FETCH_SIZE);
            // probably extra
            realLinks.setFetchSize(DEFAULT_FETCH_SIZE);
            unreachableCandidates.setFetchSize(DEFAULT_FETCH_SIZE);

            StubbleGraphContainer container = new StubbleGraphContainer(dbUser, dbServerURL);
            container.setStubNodes(stubbleNodes);
            container.setStubbleLinksGraph(stubbleLinks);
            container.setStubNodesWithRealLinks(realLinks);
            container.setUnreachableStubbles(unreachableCandidates);
            if (container.hasAnyUnreachableStubble()) {
                System.out.println(container);
            }
        } catch (SQLException e) {
            System.out.println(String.format("Unable to process dbUser = %s, dbServerURL = %s. Cause:%n%s", dbUser, dbServerURL, e));
        }
    }
}
