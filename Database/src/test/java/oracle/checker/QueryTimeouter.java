package oracle.checker;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class QueryTimeouter {
    private static final long DEFAULT_TIMEOUT = 1L;
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

    public static Connection getConnection(String dbServerUrl, String dbUser, String password) throws InterruptedException, ExecutionException, TimeoutException {
        return EXECUTOR.submit(() -> DriverManager.getConnection(dbServerUrl, dbUser, password)).get(DEFAULT_TIMEOUT, TimeUnit.MINUTES);
    }

    public static ResultSet getResultSet(Connection connection, String query) throws InterruptedException, ExecutionException, TimeoutException {
        return EXECUTOR.submit(() -> connection.createStatement().executeQuery(query)).get(DEFAULT_TIMEOUT, TimeUnit.MINUTES);
    }

    public static ResultSet getResultSet(PreparedStatement statement, String query) throws InterruptedException, ExecutionException, TimeoutException
    {
        return EXECUTOR.submit(() -> statement.executeQuery(query)).get(DEFAULT_TIMEOUT, TimeUnit.MINUTES);
    }

    public static void shutdown()
    {
        EXECUTOR.shutdown();
    }
}
