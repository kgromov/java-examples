package oracle.checker.consumers;

import oracle.checker.criterias.ICriteria;
import oracle.checker.gateways.StubbleGraphContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UnreachableStubbleLinksConsumer implements ICriteriaConsumer {
    private static final int DEFAULT_FETCH_SIZE = 1000;

    // TODO: move to criteria query
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


    @Override
    public void processDbUser(String dbUser, String dbServerURL) {
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

    @Override
    public void processDbUser(Connection connection, String dbUser, String dbServerURL) {
        try (ResultSet stubbleNodes = connection.createStatement().executeQuery(ICriteria.getQuery(STUB_NODES_QUERY, dbUser));
             ResultSet stubbleLinks = connection.createStatement().executeQuery(ICriteria.getQuery(STUB_LINKS_QUERY, dbUser));
             ResultSet realLinks = connection.createStatement().executeQuery(ICriteria.getQuery(REAL_LINKS_STUB_NODES_QUERY, dbUser));
             ResultSet unreachableCandidates = connection.createStatement().executeQuery(ICriteria.getQuery(UNREACHABLE_CANDIDATES_QUERY, dbUser))) {
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
