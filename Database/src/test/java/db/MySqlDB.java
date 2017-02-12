package db;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by konstantin on 09.01.2017.
 */
public class MySqlDB extends DataBase {
    private String user;
    private String password;

    public MySqlDB(String dbName, String user, String password) {
        this(dbName, user, password, 3306);
    }

    public MySqlDB(String dbName, String user, String password, int port) {
        this.db = dbName;
        this.user = user;
        this.password = password;
        this.connectionString = String.format("jdbc:mysql://localhost:%d/%s", port, dbName);
        createConnection();
    }

    @Override
    protected void createConnection() {
        try {
//            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(connectionString, user, password);
            statement = connection.createStatement();
        } catch (SQLException e /*| ClassNotFoundException e*/) {
            logger.severe("Unable to create connection for MS Access Driver cause of\n" + e.getMessage());
            throw new RuntimeException("Unable to create connection for MS Access Driver cause of\n" + e.getMessage());
        }
    }

}
