package oracle.signposts.gateways;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CheckUnreachableStubbles {
    private static final int DEFAULT_FETCH_SIZE = 1000;

    private static final String STUB_NODES_QUERY = "SELECT NODE_ID FROM STUB_NODE";
    private static final String STUB_LINKS_QUERY = "select link_id, ref_node_id, nonref_node_id from STUB_LINK";
    private static final String REAL_LINKS_STUB_NODES_QUERY = "select distinct link_id, ref_node_id as node_id from rdf_link " +
            "where ref_node_id in (select node_id from STUB_NODE) " +
            "union " +
            "select distinct link_id, nonref_node_id as node_id from rdf_link " +
            "where nonref_node_id in (select node_id from STUB_NODE)";
    private static final String UNREACHABLE_CANDIDATES_QUERY = "select distinct link_id, ref_node_id, nonref_node_id from STUB_LINK " +
            "where ref_node_id in (select node_id from STUB_NODE) " +
            "AND nonref_node_id in (select node_id from STUB_NODE)";


    public static void main(String[] args) {
        long start = System.nanoTime();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String dbUser = "CDCA_DEU_G8_19109";
            String dbServerURL = "jdbc:oracle:thin:@akela-eu-19109-01.civof2bffmif.us-east-1.rds.amazonaws.com:1521:orcl";
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
                if (container.hasAnyUnreachableStubble())
                {
                    System.out.println(container);
                }
                System.out.println(new Date() + "\t" + container);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            System.out.println(String.format("Time elapsed = %d ms", TimeUnit.MILLISECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS)));
        }
    }
}
