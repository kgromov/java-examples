package oracle.signposts.gateways;

import org.apache.commons.lang3.tuple.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StubbleGraphContainer {
    private static final String GATEWAY_ID_COLUMN_NAME = "STUB_ID";
    private static final String LINK_ID_COLUMN_NAME = "LINK_ID";
    private static final String NODE_ID_COLUMN_NAME = "NODE_ID";
    private static final String REF_NODE_ID_COLUMN_NAME = "REF_NODE_ID";
    private static final String NON_REF_NODE_ID_COLUMN_NAME = "NONREF_NODE_ID";

    private String userName;            // aka update region name
    private String dbServerUrl;         // seems optional

    private Set<Integer> stubNodes;
    private Set<Integer> stubLinks;
    private Set<Integer> unreachableStubbles;
    private Map<Integer, Set<Integer>> stubNodesToLinks;
    private Map<Integer, Pair<Integer, Integer>> stubbleLinksToNodes;

    public StubbleGraphContainer(String userName, String dbServerUrl) {
        this.userName = userName;
        this.dbServerUrl = dbServerUrl;
    }

    public void setStubNodes(ResultSet stubNodes) throws SQLException {
        this.stubNodes = new HashSet<>();
        while (stubNodes.next()) {
            int nodeId = stubNodes.getInt(NODE_ID_COLUMN_NAME);
            this.stubNodes.add(nodeId);
        }
        this.stubNodesToLinks = new HashMap<>(this.stubNodes.size());
    }

    // real links connected to stub nodes
    public void setStubNodesWithRealLinks(ResultSet rdfLinks) throws SQLException {
        while (rdfLinks.next()) {
            int linkId = rdfLinks.getInt(LINK_ID_COLUMN_NAME);
            int nodeId = rdfLinks.getInt(NODE_ID_COLUMN_NAME);
            // save only stub links
            if (stubNodes.contains(nodeId)) {
                stubNodesToLinks.computeIfAbsent(nodeId, links -> new HashSet<>()).add(linkId);
            }
        }
    }

    // all stubble links to nodes
    public void setStubbleLinksGraph(ResultSet stubbleLinks) throws SQLException {
        stubbleLinksToNodes = new HashMap<>();
        while (stubbleLinks.next()) {
            int linkId = stubbleLinks.getInt(LINK_ID_COLUMN_NAME);
            int refNodeId = stubbleLinks.getInt(REF_NODE_ID_COLUMN_NAME);
            int nonRefNodeId = stubbleLinks.getInt(NON_REF_NODE_ID_COLUMN_NAME);
            stubbleLinksToNodes.put(linkId, Pair.of(refNodeId, nonRefNodeId));
            // save only stub links
            if (stubNodes.contains(refNodeId)) {
                stubNodesToLinks.computeIfAbsent(refNodeId, links -> new HashSet<>()).add(linkId);
            }
            if (stubNodes.contains(nonRefNodeId)) {
                stubNodesToLinks.computeIfAbsent(nonRefNodeId, links -> new HashSet<>()).add(linkId);
            }
        }
        // save all stubbles
        stubLinks = stubbleLinksToNodes.keySet();
    }

    // unreachable links bypass
    public void setUnreachableStubbles(ResultSet unreachableCandidates) throws SQLException {
        unreachableStubbles = new HashSet<>();
        while (unreachableCandidates.next()) {
            int stubLinkId = unreachableCandidates.getInt(LINK_ID_COLUMN_NAME);
//            if (isStubbleLinkUnreachable(stubLinkId, new HashSet<>())) {
            if (isStubbleLinkUnreachable2(stubLinkId, new HashSet<>())) {
                unreachableStubbles.add(stubLinkId);
            }
        }
    }

    private boolean isStubbleLinkUnreachable(int stubLinkId, Set<Integer> processedLinkIDs) {
        // link is already processed
        processedLinkIDs.add(stubLinkId);
        // real link
        if (!stubLinks.contains(stubLinkId)) {
            return false;
        }
        Pair<Integer, Integer> stubNodeIDs = stubbleLinksToNodes.get(stubLinkId);
        // real node
        int refNodeId = stubNodeIDs.getLeft();
        int nonRefNodeId = stubNodeIDs.getRight();
        if (!stubNodes.contains(refNodeId) || !stubNodes.contains(nonRefNodeId)) {
            return false;
        }
        // recursive bypass
        if (stubNodesToLinks.getOrDefault(refNodeId, Collections.emptySet()).stream()
                .filter(linkId -> !processedLinkIDs.contains(linkId))
                .anyMatch(linkId -> isStubbleLinkUnreachable(linkId, processedLinkIDs))
                || stubNodesToLinks.getOrDefault(nonRefNodeId, Collections.emptySet()).stream()
                .filter(linkId -> !processedLinkIDs.contains(linkId))
                .anyMatch(linkId -> isStubbleLinkUnreachable(linkId, processedLinkIDs)))
        {
            return true;
        }
        return true;
    }

    private boolean isStubbleLinkUnreachable2(int stubLinkId, Set<Integer> processedLinkIDs) {
        processedLinkIDs.add(stubLinkId);
        // real link
        if (!stubLinks.contains(stubLinkId)) {
            return false;
        }
        Pair<Integer, Integer> stubNodeIDs = stubbleLinksToNodes.get(stubLinkId);
        // real node
        int refNodeId = stubNodeIDs.getLeft();
        int nonRefNodeId = stubNodeIDs.getRight();
        if (!stubNodes.contains(refNodeId) || !stubNodes.contains(nonRefNodeId)) {
            return false;
        }
        // connected links bypass
        for (int linkId : stubNodesToLinks.getOrDefault(refNodeId, Collections.emptySet())) {
            if (processedLinkIDs.contains(linkId)) {
                continue;
            }
            if (isStubbleLinkUnreachable2(linkId, processedLinkIDs)) {
                return true;
            }
        }
        // connected links bypass
        for (int linkId : stubNodesToLinks.getOrDefault(nonRefNodeId, Collections.emptySet())) {
            if (processedLinkIDs.contains(linkId)) {
                continue;
            }
            if (isStubbleLinkUnreachable2(linkId, processedLinkIDs)) {
                return true;
            }
        }
        return true;
    }

    public boolean hasAnyUnreachableStubble() {
        return unreachableStubbles != null && !unreachableStubbles.isEmpty();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StubbleGraphContainer{");
        sb.append("userName='").append(userName).append('\'');
        sb.append(", dbServerUrl='").append(dbServerUrl).append('\'');
        sb.append(", unreachableStubbles=").append(unreachableStubbles);
        sb.append('}');
        return sb.toString();
    }
}
