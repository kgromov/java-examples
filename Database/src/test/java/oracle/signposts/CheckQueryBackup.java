package oracle.signposts;

import com.google.common.collect.ImmutableMap;
import oracle.signposts.criterias.ICriteria;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class CheckQueryBackup {
    private static final Logger LOGGER = Logger.getLogger(CheckQueryBackup.class.getName());
    private static final String USERS_QUERY = "select username from dba_users WHERE username like 'CDCA_%'";
    private static final String DVN_USERS_PREDICATE = " and username like '%s'";
    private static final String DB_PASSWORD = "password";
    private static final String DB_SERVER_URL = "jdbc:oracle:thin:@akela-%s-%s-0%d.civof2bffmif.us-east-1.rds.amazonaws.com:1521:orcl";
    private static final Map<String, String> MARKET_TO_DVN = ImmutableMap.<String, String>builder()
            .put("eu", "19109")
            .put("nar", "191F0")
            .put("mrm", "191E3")
            .build();

    public static void main(String[] args) {
        long start = System.nanoTime();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            ICriteria criteria = StubbleCriteria.STUB_LOCAL_POI;
            MARKET_TO_DVN.forEach((market, dvn) ->
            {
                LOGGER.info(String.format("################## market = %s, dvn = %s ##################", market, dvn));
//                Set<String> allUsers = new UsersReader(market).getCdcUsers();
                Set<String> allUsers = new UsersReader(market).getCdcUserWithDVN(dvn);
                Set<String> iterateUsers = new HashSet<>(allUsers);
                for (int i = 1; i < 9; i++) {
                    String dbServerUrl = String.format(DB_SERVER_URL, market, dvn, i)
                            // exceptional case
                            .replaceAll("_", "-");
                    LOGGER.info("Start processing dbServer = " + dbServerUrl);
                    allUsers.stream().filter(iterateUsers::contains).forEach(cdcUser ->
                    {
                        Set<String> dbServerUsers = new HashSet<>();
                        try (Connection connection = DriverManager.getConnection(dbServerUrl, cdcUser, DB_PASSWORD);
                             ResultSet userNames = connection.createStatement().executeQuery(USERS_QUERY)) {
                            while (userNames.next()) {
                                dbServerUsers.add(userNames.getString(1));
                            }
                            // users
                            dbServerUsers.parallelStream().forEach(userName ->
                            {
                                try (Connection connection2 = DriverManager.getConnection(dbServerUrl, userName, DB_PASSWORD);
                                     ResultSet queryResult2 = connection2.createStatement().executeQuery(criteria.getQuery())) {
                                    if (queryResult2.next()) {
                                        System.out.println(String.format("SourceDbUser = %s, dbServer = %s", userName, dbServerUrl));
                                        System.out.println("Criteria is found! E.g. :" + criteria.getIdentity(queryResult2));
                                    }
                                    synchronized (iterateUsers) {
                                        iterateUsers.remove(userName);
                                    }
                                } catch (SQLException e) {
                                     LOGGER.severe(String.format("Unable to process dbUser = %s, dbServerURL = %s. Cause:%n%s", userName, dbServerUrl, e));

                                }
                            });
                        } catch (Exception e) {
//                            System.err.println(String.format("No SourceDbUser = %s, dbServer = %s", cdcUser, dbServerUrl));
                        }
                    });
                    LOGGER.info("Finish processing dbServer = " + dbServerUrl);
                }
                LOGGER.info("Remaining users: " + iterateUsers);
            });
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            LOGGER.info(String.format("Time elapsed = %d s", TimeUnit.SECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS)));
        }
    }
}
