package oracle.signposts;

import com.google.common.collect.ImmutableMap;

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
    private static final String SIGNPOST_BLOB_QUERY = "select FILE_ID from CDC_RDF_FILE_2D_SIGN " +
            "where REGEXP_LIKE (utl_raw.cast_to_varchar2(dbms_lob.substr(FILE_OBJECT, 2000, 2001)), '(dx)|(dy)')";
    private static final String DB_SERVER_URL = "jdbc:oracle:thin:@akela-%s-%s-0%d.civof2bffmif.us-east-1.rds.amazonaws.com:1521:orcl";
    private static final Map<String, String> MARKET_TO_DVN = ImmutableMap.<String, String>builder()
            .put("eu", "191E0")
            .put("nar", "18148")
            .put("mrm", "18144")
            .build();

    public static void main(String[] args) {
        long start = System.nanoTime();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String market = "mrm";
            String dvn = MARKET_TO_DVN.getOrDefault(market, "mrm");
            Set<String> allUsers = new UsersReader(market).getCdcUsers();
            Set<String> iterateUsers = new HashSet<>(allUsers);
            for (int i = 1; i < 9; i++) {
                String dbServerUrl = String.format(DB_SERVER_URL, market, dvn, i);
                System.out.println("Start processing dbServer = " + dbServerUrl);
                allUsers.stream().filter(iterateUsers::contains).forEach(cdcUser ->
                {
                    Set<String> dbServerUsers = new HashSet<>();
                    try (Connection connection = DriverManager.getConnection(dbServerUrl, cdcUser, "password");
                         ResultSet userNames = connection.createStatement().executeQuery(USERS_QUERY);
                         ResultSet signposts = connection.createStatement().executeQuery(SIGNPOST_BLOB_QUERY)) {
                        System.out.println(String.format("SourceDbUser = %s, dbServer = %s", cdcUser, dbServerUrl));
                        while (userNames.next()) {
                            dbServerUsers.add(userNames.getString(1));
                        }
                        if (signposts.next()) {
                            System.out.println("dx/dy are found! E.g. :" + signposts.getObject(1));
                        }
                        synchronized (iterateUsers) {
                            iterateUsers.remove(cdcUser);
                        }
                        // users
                        dbServerUsers.parallelStream().forEach(userName ->
                        {
                            try (Connection connection2 = DriverManager.getConnection(dbServerUrl, userName, "password");
                                 ResultSet signposts2 = connection2.createStatement().executeQuery(SIGNPOST_BLOB_QUERY)) {
                                System.out.println(String.format("SourceDbUser = %s, dbServer = %s", userName, dbServerUrl));
                                if (signposts2.next()) {
                                    System.out.println("dx/dy are found! E.g. :" + signposts2.getObject(1));
                                }
                                synchronized (iterateUsers) {
                                    iterateUsers.remove(userName);
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        });
                    } catch (Exception e) {
//                        System.err.println(String.format("No SourceDbUser = %s, dbServer = %s", cdcUser, dbServerUrl));
                    }
                });
                System.out.println("Finish processing dbServer = " + dbServerUrl);
            }
            System.out.println("Remaining users: " + iterateUsers);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            System.out.println(String.format("Time elapsed = %d s", TimeUnit.SECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS)));
        }
    }
}
