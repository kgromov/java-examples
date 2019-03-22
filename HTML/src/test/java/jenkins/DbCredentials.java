package jenkins;

public class DbCredentials {
    private String sourceDbUser;
    private String dbUser;
    private String dBServer;

    public DbCredentials() {
    }

    public DbCredentials(String sourceDbUser, String dbUser, String dBServer) {
        this.sourceDbUser = sourceDbUser;
        this.dbUser = dbUser;
        this.dBServer = dBServer;
    }

    public String getSourceDbUser() {
        return sourceDbUser == null ? "" : sourceDbUser;
    }

    public String getDbUser() {
        return dbUser == null ? "" : dbUser;
    }

    public String getdBServer() {
        return dBServer == null ? "" : dBServer;
    }

    public void setSourceDbUser(String sourceDbUser) {
        this.sourceDbUser = sourceDbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public void setdBServer(String dBServer) {
        this.dBServer = dBServer;
    }

    protected boolean areCredentialsSet() {
        return dbUser != null && dBServer != null && sourceDbUser != null;
    }

    @Override
    public String toString() {
        return new StringBuilder("DbCredentials{")
                .append("SourceDBUser=").append(sourceDbUser)
                .append(", DBUser=").append(dbUser)
                .append(", DBServer=").append(dBServer)
                .toString();
    }
}
