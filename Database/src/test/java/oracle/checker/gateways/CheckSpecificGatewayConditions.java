package oracle.checker.gateways;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.tuple.Pair;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class CheckSpecificGatewayConditions {
    private static final String LINK_ID_COLUMN_NAME = "LINK_ID";
    private static final String GATEWAY_ID_COLUMN_NAME = "STUB_ID";

    private static final String STUB_LINKS_QUERY = QueryCondition.TRANSITION_MASK_CONDITIONS.getStubbleGatewaysQuery();
    private static final String LOCAL_LINKS_QUERY = QueryCondition.TRANSITION_MASK_CONDITIONS.getLocalGatewaysQuery();

    private static Pair<String, String> sourceRegionConfig = Pair.of("CDCA_DEU_G2_HE_191E0",
            "jdbc:oracle:thin:@akela-eu-191E0-02.civof2bffmif.us-east-1.rds.amazonaws.com:1521:orcl"); // 10403

    private static Map<String, String> neighbourRegionsConfig = new ImmutableMap.Builder<String, String>()
            .put("CDCA_DEU_G1_NO_191E0", "jdbc:oracle:thin:@akela-eu-191E0-07.civof2bffmif.us-east-1.rds.amazonaws.com:1521:orcl")      // 10402
            .put("CDCA_DEU_G2_RP_SA_191E0", "jdbc:oracle:thin:@akela-eu-191E0-03.civof2bffmif.us-east-1.rds.amazonaws.com:1521:orcl")   // 10404
            .put("CDCA_DEU_G3_191E0", "jdbc:oracle:thin:@akela-eu-191E0-05.civof2bffmif.us-east-1.rds.amazonaws.com:1521:orcl")         // 10405
            .put("CDCA_DEU_G4_SA_SO_191E0", "jdbc:oracle:thin:@akela-eu-191E0-06.civof2bffmif.us-east-1.rds.amazonaws.com:1521:orcl")   // 10406
            .put("CDCA_DEU_G5_191E0", "jdbc:oracle:thin:@akela-eu-191E0-07.civof2bffmif.us-east-1.rds.amazonaws.com:1521:orcl")         // 10409
            .put("CDCA_DEU_G6_NO_191E0", "jdbc:oracle:thin:@akela-eu-191E0-04.civof2bffmif.us-east-1.rds.amazonaws.com:1521:orcl")      // 10411
            .put("CDCA_DEU_G8_191E0", "jdbc:oracle:thin:@akela-eu-191E0-01.civof2bffmif.us-east-1.rds.amazonaws.com:1521:orcl")         // 10413
            .build();

    public static void main(String[] args) {
        long start = System.nanoTime();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            SourceUpdateRegionGatewayLinksHolder sourceGatewaysHolder = new SourceUpdateRegionGatewayLinksHolder(
                    sourceRegionConfig.getLeft(), sourceRegionConfig.getRight()
            );
            // init source link gateways
            try (Connection connection = DriverManager.getConnection(sourceGatewaysHolder.getDbServerUrl(),
                    sourceGatewaysHolder.getUserName(), "password");
                 ResultSet stubbleGateways = connection.createStatement().executeQuery(STUB_LINKS_QUERY);
                 ResultSet localGateways = connection.createStatement().executeQuery(LOCAL_LINKS_QUERY)) {
                sourceGatewaysHolder.setStubbleGateways(stubbleGateways);
                sourceGatewaysHolder.setLocalGateways(localGateways);
                System.out.println(new Date() + "\t" + sourceGatewaysHolder);

            } catch (SQLException e) {
                e.printStackTrace();
            }
            // neighbours
            neighbourRegionsConfig.entrySet().parallelStream().forEach(entry ->
            {
                String user = entry.getKey();
                String dbServerUrl = entry.getValue();
                try (Connection connection = DriverManager.getConnection(dbServerUrl, user, "password");
                     ResultSet stubbleGateways = connection.createStatement().executeQuery(STUB_LINKS_QUERY);
                     ResultSet localGateways = connection.createStatement().executeQuery(LOCAL_LINKS_QUERY)) {
                    Set<Pair<Integer, Integer>> stubbles = getLinkGateways(stubbleGateways);
                    Set<Pair<Integer, Integer>> locals = getLinkGateways(localGateways);
                    System.out.println(String.format("%s\tuser = %s, dbServer = %s%nstubbleGateways = %d%nlocalGateways = %d",
                            new Date(), user, dbServerUrl, stubbles.size(), locals.size()));
                    sourceGatewaysHolder.getStubbleGateways().removeAll(locals);
                    sourceGatewaysHolder.getLocalGateways().removeAll(stubbles);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            System.out.println(new Date() +"\tRemaining link gateways: " + sourceGatewaysHolder);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            System.out.println(String.format("Time elapsed = %d ms", TimeUnit.MILLISECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS)));
        }
    }

    private static Set<Pair<Integer, Integer>> getLinkGateways(ResultSet resultSet) throws SQLException {
        Set<Pair<Integer, Integer>> linkGateways = new HashSet<>();
        while (resultSet.next()) {
            int linkId = resultSet.getInt(LINK_ID_COLUMN_NAME);
            int gatewayId = resultSet.getInt(GATEWAY_ID_COLUMN_NAME);
            linkGateways.add(Pair.of(linkId, gatewayId));
        }
        return linkGateways;
    }
}
