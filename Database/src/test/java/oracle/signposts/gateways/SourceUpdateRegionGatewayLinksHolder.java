package oracle.signposts.gateways;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.tuple.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public class SourceUpdateRegionGatewayLinksHolder {
    private static final String LINK_ID_COLUMN_NAME = "LINK_ID";
    private static final String GATEWAY_ID_COLUMN_NAME = "STUB_ID";

    private String userName;            // aka update region name
    private String dbServerUrl;         // seems optional
    private Set<Pair<Integer, Integer>> localGateways;
    private Set<Pair<Integer, Integer>> stubbleGateways;

    public SourceUpdateRegionGatewayLinksHolder(String userName, String dbServerUrl) {
        this.userName = userName;
        this.dbServerUrl = dbServerUrl;
    }

    public String getUserName() {
        return userName;
    }

    public String getDbServerUrl() {
        return dbServerUrl;
    }

    public synchronized Set<Pair<Integer, Integer>> getLocalGateways() {
        return localGateways;
    }

    public void setLocalGateways(Set<Pair<Integer, Integer>> localGateways) {
        this.localGateways = localGateways;
    }

    public synchronized Set<Pair<Integer, Integer>> getStubbleGateways() {
        return stubbleGateways;
    }

    public void setStubbleGateways(Set<Pair<Integer, Integer>> stubbleGateways) {
        this.stubbleGateways = stubbleGateways;
    }

    public void setLocalGateways(ResultSet localGateways) throws SQLException {
        this.localGateways = Sets.newHashSet();
        while (localGateways.next())
        {
            int linkId = localGateways.getInt(LINK_ID_COLUMN_NAME);
            int gatewayId = localGateways.getInt(GATEWAY_ID_COLUMN_NAME);
            this.localGateways.add(Pair.of(linkId, gatewayId));
        }
    }

    public void setStubbleGateways(ResultSet stubbleGateways) throws SQLException {
        this.stubbleGateways = Sets.newHashSet();
        while (stubbleGateways.next())
        {
            int linkId = stubbleGateways.getInt(LINK_ID_COLUMN_NAME);
            int gatewayId = stubbleGateways.getInt(GATEWAY_ID_COLUMN_NAME);
            this.stubbleGateways.add(Pair.of(linkId, gatewayId));
        }
    }

    @Override
    public String toString() {
        return new StringBuilder("SourceUpdateRegionGatewayLinks{")
                .append("userName='").append(userName).append('\'')
                .append(", dbServerUrl='").append(dbServerUrl).append('\'')
                .append("\nlocalGateways=").append(localGateways.size()).append(":").append(localGateways)
                .append("\nstubbleGateways=").append(stubbleGateways.size()).append(":").append(stubbleGateways)
                .append('}').toString();
    }
}
