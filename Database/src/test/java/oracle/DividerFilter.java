package oracle;

import com.google.common.base.Objects;
import org.apache.commons.lang3.tuple.Triple;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DividerFilter {
    // get all pairs: RR, RS, SR. But to map stubble dividers and nodes STUBBLE_LINKS_WITH_DIVIDER_QUERY is required
    private static final String REAL_LINKS_WITH_DIVIDER_QUERY = "select rnl.LINK_ID, rnl.DIVIDER_LEGAL, rnl.DIVIDER, rl.REF_NODE_ID, rl.NONREF_NODE_ID from RDF_NAV_LINK rnl " +
            "join RDF_LINK rl on rl.LINK_ID = rnl.LINK_ID " +
            "where rnl.DIVIDER not in ('N', 'L') " +
            "and (rnl.LINK_ID in (select FROM_LINK_ID from RDF_CONDITION_DIVIDER) " +
            "or rnl.LINK_ID in (select TO_LINK_ID from RDF_CONDITION_DIVIDER))";

    private static final String STUBBLE_LINKS_WITH_DIVIDER_QUERY = "select rnl.LINK_ID, rnl.DIVIDER_LEGAL, rnl.DIVIDER, rl.REF_NODE_ID, rl.NONREF_NODE_ID from STUB_NAV_LINK rnl " +
            "join STUB_LINK rl on rl.LINK_ID = rnl.LINK_ID " +
            "where rnl.DIVIDER not in ('N', 'L') " +
            "and (rnl.LINK_ID in (select FROM_LINK_ID from RDF_CONDITION_DIVIDER) " +
            "or rnl.LINK_ID in (select TO_LINK_ID from RDF_CONDITION_DIVIDER))";

    private Set<Triple<Integer, Integer, Integer>> usedDividers;
    private Map<Integer, LinkInfo> realLinksInfo;
    private Map<Integer, LinkInfo> stubbleLinksInfo;


    private AtomicInteger notSaved = new AtomicInteger();
    private AtomicInteger saved = new AtomicInteger();

    public DividerFilter() {
    }

    public void processRealLinks(ResultSet resultSet) throws SQLException {
        realLinksInfo = new HashMap<>();
        processLinksWithDivider(resultSet, realLinksInfo);
    }

    public void processStubbleLinks(ResultSet resultSet) throws SQLException {
        stubbleLinksInfo = new HashMap<>();
        processLinksWithDivider(resultSet, stubbleLinksInfo);
    }

    private void processLinksWithDivider(ResultSet resultSet, Map<Integer, LinkInfo> linksInfo) throws SQLException {
        while (resultSet.next()) {
            int linkId = resultSet.getInt("LINK_ID");
            int refNodeId = resultSet.getInt("REF_NODE_ID");
            int nonRefNodeId = resultSet.getInt("NONREF_NODE_ID");
            boolean isDividerLegal = resultSet.getString("DIVIDER_LEGAL").equalsIgnoreCase("Y");
            char dividerType = resultSet.getString("DIVIDER").charAt(0);

            LinkInfo linkInfo = new LinkInfo(linkId, refNodeId, nonRefNodeId, isDividerLegal, dividerType);
            linksInfo.put(linkId, linkInfo);
        }
    }

    // as an idea - left join row from divider where nodeId is common with real links -> border_node
    private void saveDividers(ResultSet dividers) throws SQLException {
        usedDividers = new HashSet<>();
        while (dividers.next()) {
            int nodeId = dividers.getInt("NODE_ID");
            int fromLinkId = dividers.getInt("FROM_LINK_ID");
            int toLinkId = dividers.getInt("TO_LINK_ID");
            // both stubble case
            LinkInfo stubbleLinkFrom = stubbleLinksInfo.get(fromLinkId);
            LinkInfo stubbleLinkTo = stubbleLinksInfo.get(toLinkId);
            if (stubbleLinkFrom != null && stubbleLinkTo != null) {
                notSaved.incrementAndGet();
                System.out.println(String.format("Not saved: NODE_ID = %d, FROM_LINK_ID = %d, TO_LINK_ID = %d",
                        nodeId, fromLinkId, toLinkId));
                continue;
            }
            // divider is not in use
            LinkInfo realLinkFrom = realLinksInfo.get(fromLinkId);
            LinkInfo realLinkTo = realLinksInfo.get(toLinkId);

            if (realLinkFrom == null && realLinkTo == null) {
                notSaved.incrementAndGet();
                System.out.println(String.format("Not saved: NODE_ID = %d, FROM_LINK_ID = %d, TO_LINK_ID = %d",
                        nodeId, fromLinkId, toLinkId));
                continue;
            }

            /*if (isDividerInUse(realLinkFrom, realLinkTo, nodeId)
                    || isDividerInUse(realLinkFrom, stubbleLinkTo, nodeId)
                    || isDividerInUse(stubbleLinkFrom, realLinkTo, nodeId)) {
                usedDividers.put(nodeId, Pair.of(fromLinkId, toLinkId));
            }*/

           /* if (isDividerInUse(realLinkFrom, nodeId)
                    || isDividerInUse(realLinkTo, nodeId)
                    || isDividerInUse(stubbleLinkFrom, nodeId)
                    || isDividerInUse(stubbleLinkTo, nodeId)) {
                usedDividers.put(nodeId, Pair.of(fromLinkId, toLinkId));
            }*/

            boolean isSaved = isDividerInUse(realLinkFrom, nodeId)
                    || isDividerInUse(realLinkTo, nodeId)
                    || isDividerInUse(stubbleLinkFrom, nodeId)
                    || isDividerInUse(stubbleLinkTo, nodeId);
            if (isSaved) {
                saved.incrementAndGet();
                usedDividers.add(Triple.of(nodeId, fromLinkId, toLinkId));
            } else {
                notSaved.incrementAndGet();
                System.out.println(String.format("Not saved: NODE_ID = %d, FROM_LINK_ID = %d, TO_LINK_ID = %d",
                        nodeId, fromLinkId, toLinkId));
            }
        }
    }

    private boolean isDividerInUse(LinkInfo linkFrom, LinkInfo linkTo, int nodeId) {
        if (linkFrom == null || linkTo == null) {
            return false;
        }
        boolean isRefNodeFrom = linkFrom.getRefNodeId() == nodeId;
        boolean isRefNodeTo = linkTo.getRefNodeId() == nodeId;
        return (!linkFrom.isDividerLegal || !linkTo.isDividerLegal)
                || (linkFrom.getDividerType() == 'A' || linkFrom.getDividerType() == 'A')
                || (isRefNodeFrom && linkFrom.getDividerType() == '1')
                || (!isRefNodeFrom && linkFrom.getDividerType() == '2')
                || (isRefNodeTo && linkTo.getDividerType() == '1')
                || (!isRefNodeTo && linkTo.getDividerType() == '2');
    }

    private boolean isDividerInUse(LinkInfo linkFromOrTo, int nodeId) {
        return linkFromOrTo != null
                && (!linkFromOrTo.isDividerLegal()
                || linkFromOrTo.getDividerType() == 'A'
                || linkFromOrTo.getRefNodeId() == nodeId && linkFromOrTo.getDividerType() == '1'
                || linkFromOrTo.getNonRefNodeId() == nodeId && linkFromOrTo.getDividerType() == '2');
    }

    // rnl.LINK_ID, rnl.DIVIDER_LEGAL, rnl.DIVIDER, rl.REF_NODE_ID, rl.NONREF_NODE_ID
    private static void exportToCsv() {

    }

    // change in compiler to Divider.class
    private static final class LinkInfo {
        private int linkId;
        private int refNodeId;
        private int nonRefNodeId;
        private boolean isDividerLegal;
        private char dividerType;

        public LinkInfo(int linkId, int refNodeId, int nonRefNodeId, boolean isDividerLegal, char dividerType) {
            this.linkId = linkId;
            this.refNodeId = refNodeId;
            this.nonRefNodeId = nonRefNodeId;
            this.isDividerLegal = isDividerLegal;
            this.dividerType = dividerType;
        }

        public int getLinkId() {
            return linkId;
        }

        public int getRefNodeId() {
            return refNodeId;
        }

        public int getNonRefNodeId() {
            return nonRefNodeId;
        }

        public boolean isDividerLegal() {
            return isDividerLegal;
        }

        public char getDividerType() {
            return dividerType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LinkInfo linkInfo = (LinkInfo) o;
            return linkId == linkInfo.linkId &&
                    refNodeId == linkInfo.refNodeId &&
                    nonRefNodeId == linkInfo.nonRefNodeId &&
                    isDividerLegal == linkInfo.isDividerLegal &&
                    dividerType == linkInfo.dividerType;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(linkId, refNodeId, nonRefNodeId, isDividerLegal, dividerType);
        }

        @Override
        public String toString() {
            return new StringBuilder("LinkInfo{")
                    .append("linkId=").append(linkId)
                    .append(", refNodeId=").append(refNodeId)
                    .append(", nonRefNodeId=").append(nonRefNodeId)
                    .append(", isDividerLegal=").append(isDividerLegal)
                    .append(", dividerType=").append(dividerType)
                    .append('}').toString();
        }
    }


    /*
       private static Pair<String, String> sourceRegionConfig = Pair.of("CDCA_DEU_G2_HE_18144",
            "jdbc:oracle:thin:@akela-eu-18144-02.civof2bffmif.us-east-1.rds.amazonaws.com:1521:orcl");

        private static Map<String, String> neighbourRegionsConfig = new ImmutableMap.Builder<String, String>()
            .put("CDCA_DEU_G1_NO_18144", "jdbc:oracle:thin:@akela-eu-18144-07.civof2bffmif.us-east-1.rds.amazonaws.com:1521:orcl")
            .put("CDCA_DEU_G2_RP_SA_18144", "jdbc:oracle:thin:@akela-eu-18144-03.civof2bffmif.us-east-1.rds.amazonaws.com:1521:orcl")
            .put("CDCA_DEU_G3_18144", "jdbc:oracle:thin:@akela-eu-18144-05.civof2bffmif.us-east-1.rds.amazonaws.com:1521:orcl")
            .put("CDCA_DEU_G4_SA_SO_18144", "jdbc:oracle:thin:@akela-eu-18144-06.civof2bffmif.us-east-1.rds.amazonaws.com:1521:orcl")
            .put("CDCA_DEU_G5_18144", "jdbc:oracle:thin:@akela-eu-18144-07.civof2bffmif.us-east-1.rds.amazonaws.com:1521:orcl")
            .put("CDCA_DEU_G6_NO_18144", "jdbc:oracle:thin:@akela-eu-18144-04.civof2bffmif.us-east-1.rds.amazonaws.com:1521:orcl")
            .put("CDCA_DEU_G8_18144", "jdbc:oracle:thin:@akela-eu-18144-01.civof2bffmif.us-east-1.rds.amazonaws.com:1521:orcl")
            .build();
     */
    public static void main(String[] args) {
        long start = System.nanoTime();
        try {
            String user = "CDCA_DEU_G2_HE_18144";
            String dbServerUrl = "jdbc:oracle:thin:@akela-eu-18144-02.civof2bffmif.us-east-1.rds.amazonaws.com:1521:orcl";
            Class.forName("oracle.jdbc.driver.OracleDriver");
            try (Connection connection = DriverManager.getConnection(dbServerUrl, user, "password");
                 ResultSet stubbleGateways = connection.createStatement().executeQuery(STUBBLE_LINKS_WITH_DIVIDER_QUERY);
                 ResultSet localGateways = connection.createStatement().executeQuery(REAL_LINKS_WITH_DIVIDER_QUERY);
                 ResultSet rdfDividers = connection.createStatement().executeQuery("select NODE_ID, FROM_LINK_ID, TO_LINK_ID from RDF_CONDITION_DIVIDER")) {
                DividerFilter filter = new DividerFilter();
                filter.processRealLinks(localGateways);
                filter.processStubbleLinks(stubbleGateways);
                filter.saveDividers(rdfDividers);
                System.out.println(filter.usedDividers.size());
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
