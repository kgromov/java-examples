package db;

import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by konstantin on 08.01.2017.
 */
public abstract class DataBase {
    protected final Logger logger = Logger.getLogger(this.getClass().getName());
    protected Connection connection;
    protected Statement statement;
    protected String connectionString;
    protected String db;

    protected abstract void createConnection();

    protected ResultSet executeQuery(String sqlQuery) {
        try {
            logger.info("Execute query:\t" + sqlQuery);
            return statement.executeQuery(sqlQuery);
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Unable to execute SQL query '%s'\nCause of: %s", sqlQuery, e.getMessage()));
        }
    }

    public boolean insert(String tableName, String column, String value) {
        return false;
    }

    public void insert(String tableName, List<String> columns, List<String> values) {
        long start = System.currentTimeMillis();
        String query = String.format("INSERT INTO %s.%s (%s) VALUES (%s)", db, tableName, StringUtils.join(columns, ","), StringUtils.join(values, ","));
        try {
            statement.executeUpdate(query);
            logger.info(String.format("Time to execute 1 insert via statement with DML query directly = %d ms", System.currentTimeMillis() - start));
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Unable to execute update SQL query '%s'\nCause of: %s", query, e.getMessage()));
        }
    }

    public void insertWithPreparedStatement(PreparedStatement statement, List<String> values) {
        long start = System.currentTimeMillis();
        try {
            for (int i = 1; i <= values.size(); i++) {
                statement.setObject(i, values.get(i - 1));
            }
            statement.executeUpdate();
            logger.info(String.format("Time to execute 1 insert via PreparedStatement with DML = %d ms", System.currentTimeMillis() - start));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertWithPreparedStatementAndBatch(PreparedStatement statement, List<String> values) {
        long start = System.currentTimeMillis();
        try {
            for (int i = 1; i <= values.size(); i++) {
                statement.setObject(i, values.get(i - 1));
            }
            statement.addBatch();
            logger.info(String.format("Time to add to batch 1 insert via PreparedStatement = %d ms", System.currentTimeMillis() - start));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean update() {
        return false;
    }

    public String getValue() {
        return null;
    }

    public List<String> getValues() {
        return null;
    }

    //SELECT * FROM table limit 100, 200` -- get 200 records beginning with row 101
    public void printTable(String tableName, List<String> columns) {
        String query = String.format("SELECT * FROM %s.%s", db, tableName);
        long start = System.currentTimeMillis();
        ResultSet resultSet = executeQuery(query);
        System.out.println(String.format("Time to select all rows = %d", System.currentTimeMillis() - start));
        System.out.println(String.format("Total rows = %d", getRowsCount(tableName)));
       /* try {
            while (resultSet.next()) {
                for (String column : columns) {
                    System.out.print("\t" + column + " = " + resultSet.getObject(column));
                }
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }

    public int getRowsCount(String tableName) {
        int count = -1;
        String query = "SELECT COUNT(*) FROM " + db + "." + tableName + ";";
        ResultSet resultSet = executeQuery(query);
        try {
            while (resultSet.next())
                count = Integer.parseInt(resultSet.getString(1));
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Unable to execute SQL query '%s'\nCause of: %s", query, e.getMessage()));
        }
        logger.info(String.format("Rows count = '%d'", count));
        return count;
    }

    public void closeConnection() {
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            logger.severe(String.format("Unable to close connection for '%s' Driver, DB '%s' cause of\n%s", this.getClass().getSimpleName(), db, e.getMessage()));
        }
    }

    // temp
    public Connection getConnection() {
        return connection;
    }
}
