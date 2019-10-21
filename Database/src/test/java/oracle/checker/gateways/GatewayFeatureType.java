package oracle.checker.gateways;

public enum GatewayFeatureType {
    BORDER_LINK ("SELECT STUB_ID FROM %s.SC_BORDER_LINK"),
    STUB_LINK("SELECT STUB_ID FROM %s.STUB_LINK"),
    STUB_LOCAL_LINK("SELECT STUB_ID FROM %s.STUB_LINK_LOCAL"),
    BORDER_NODE("SELECT STUB_ID FROM %s.SC_BORDER_NODE"),
    STUB_NODE("SELECT STUB_ID FROM %s.STUB_NODE"),
    STUB_LOCAL_NODE("SELECT STUB_ID FROM %s.STUB_NODE_LOCAL");

    private String query;

    GatewayFeatureType(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public String getQuery(String user) {
        return String.format(query, user);
    }
}
