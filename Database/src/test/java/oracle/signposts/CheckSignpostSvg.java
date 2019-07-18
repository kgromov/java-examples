package oracle.signposts;

import com.google.common.collect.ImmutableMap;
import oracle.signposts.criterias.GatewaysCriteria;
import oracle.signposts.criterias.ICriteria;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class CheckSignpostSvg {
    private static final String USERS_QUERY = "select username from dba_users WHERE username like 'CDCA_%'";
    private static final String DVN_USERS_PREDICATE = " and username like '%s'";
    private static final String DB_PASSWORD = "password";
    private static final String DB_SERVER_URL = "jdbc:oracle:thin:@akela-%s-%s-0%d.civof2bffmif.us-east-1.rds.amazonaws.com:1521:orcl";
    private static final Map<String, String> MARKET_TO_DVN = ImmutableMap.<String, String>builder()
            .put("eu", "19105_2")
            .put("nar", "191E0_2")
            .put("mrm", "191E0_2")
            .build();

    public static void main(String[] args) {
        long start = System.nanoTime();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            ICriteria criteria = GatewaysCriteria.ADMIN_WIDE_REGULATIONS;
            MARKET_TO_DVN.forEach((market, dvn) ->
            {
                System.out.println(String.format("################## market = %s, dvn = %s ##################", market, dvn));
//                Set<String> allUsers = new UsersReader(market).getCdcUsers();
                Set<String> allUsers = new UsersReader(market).getCdcUserWithDVN(dvn);
                Set<String> iterateUsers = new HashSet<>(allUsers);
                for (int i = 1; i < 9; i++) {
                    String dbServerUrl = String.format(DB_SERVER_URL, market, dvn, i)
                            // exceptional case
                            .replaceAll("_", "-");
                    System.out.println("Start processing dbServer = " + dbServerUrl);
                    allUsers.stream().filter(iterateUsers::contains).forEach(cdcUser ->
                    {
                        Set<String> dbServerUsers = new HashSet<>();
                        try (Connection connection = DriverManager.getConnection(dbServerUrl, cdcUser, DB_PASSWORD);
                             ResultSet userNames = connection.createStatement().executeQuery(USERS_QUERY);
                             ResultSet queryResult = connection.createStatement().executeQuery(criteria.getQuery())) {
                            while (userNames.next()) {
                                dbServerUsers.add(userNames.getString(1));
                            }
                            if (queryResult.next()) {
                                System.out.println(String.format("SourceDbUser = %s, dbServer = %s", cdcUser, dbServerUrl));
                                System.out.println("Criteria is found! E.g. :" + criteria.getIdentity(queryResult));
                            }
                            synchronized (iterateUsers) {
                                iterateUsers.remove(cdcUser);
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
                                    e.printStackTrace();
                                }
                            });
                        } catch (Exception e) {
//                            System.err.println(String.format("No SourceDbUser = %s, dbServer = %s", cdcUser, dbServerUrl));
                        }
                    });
                    System.out.println("Finish processing dbServer = " + dbServerUrl);
                }
                System.out.println("Remaining users: " + iterateUsers);
            });
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            System.out.println(String.format("Time elapsed = %d s", TimeUnit.SECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS)));
        }
    }
}
