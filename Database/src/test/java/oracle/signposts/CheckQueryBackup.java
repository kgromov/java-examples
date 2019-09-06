package oracle.signposts;

import com.google.common.collect.ImmutableMap;
import oracle.signposts.criterias.ICriteria;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

public class CheckQueryBackup {
    // FIXME: till Log4J be added
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");
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
            StubbleCriteria stubPoiCriteria = StubbleCriteria.STUB_POI;
            StubbleCriteria stubLocalPoiCriteria = StubbleCriteria.STUB_LOCAL_POI;
            /*StubbleCriteria stubPoiCriteria  = StubbleCriteria.STUB_POI_COUNT_DIFF;
            StubbleCriteria stubLocalPoiCriteria  = StubbleCriteria.STUB_LOCAL_POI_COUNT_DIFF;*/
            MARKET_TO_DVN.forEach((market, dvn) ->
            {
                System.out.println(String.format("################## market = %s, dvn = %s ##################", market, dvn));
                Set<String> allUsers = new UsersReader(market).getCdcUserWithDVN(dvn);
                Set<String> iterateUsers = new HashSet<>(allUsers);
                // specific
                Set<Integer> stubPoiLinks = new TreeSet<>();
                Set<Integer> stubLocalPoiLinks = new TreeSet<>();
                for (int i = 1; i < 9; i++) {
                    String dbServerUrl = String.format(DB_SERVER_URL, market, dvn, i)
                            // exceptional case
                            .replaceAll("_", "-");
                    info("Start processing dbServer = " + dbServerUrl);
                    allUsers.stream().filter(iterateUsers::contains).forEach(cdcUser ->
                    {
                        Set<String> dbServerUsers = new HashSet<>();
                        try (Connection connection = DriverManager.getConnection(dbServerUrl, cdcUser, DB_PASSWORD);
                             ResultSet userNames = connection.createStatement().executeQuery(USERS_QUERY)) {
                            while (userNames.next()) {
                                dbServerUsers.add(userNames.getString(1));
                            }
                            // users
                            dbServerUsers.stream().forEach(userName ->
                            {
                                try (ResultSet queryResult1 = connection.createStatement().executeQuery(stubPoiCriteria.getQuery(userName));
                                     ResultSet queryResult2 = connection.createStatement().executeQuery(stubLocalPoiCriteria.getQuery(userName))) {
                                    // specific
                                    Set<Integer> stubPoiLinksPerUser = new HashSet<>();
                                    Set<Integer> stubLocalPoiLinksPerUser = new HashSet<>();
                                    while (queryResult1.next()) {
                                        stubPoiLinksPerUser.add(queryResult1.getInt("LINK_ID"));
                                    }
                                    while (queryResult2.next()) {
                                        stubLocalPoiLinksPerUser.add(queryResult2.getInt("LINK_ID"));
                                    }
                                    if (!stubPoiLinksPerUser.isEmpty() || !stubLocalPoiLinksPerUser.isEmpty()) {
                                        info(String.format("SourceDbUser = %s, dbServer = %s", userName, dbServerUrl));
                                        System.out.println("STUB_POI:\t" + stubPoiLinksPerUser);
                                        System.out.println("STUB_LOCAL_POI:\t" + stubLocalPoiLinksPerUser);
                                    }

                                   /* boolean isLogged = false;
                                    if (queryResult1.next()) {
                                        System.out.println(String.format("SourceDbUser = %s, dbServer = %s", userName, dbServerUrl));
                                        System.out.println("Criteria is found! E.g. :" + stubPoiCriteria.getIdentity(queryResult1));
                                        isLogged = true;
                                    }
                                    if (queryResult2.next()) {
                                        if(!isLogged)
                                        {
                                            System.out.println(String.format("SourceDbUser = %s, dbServer = %s", userName, dbServerUrl));
                                        }
                                        System.out.println("Criteria is found! E.g. :" + stubLocalPoiCriteria.getIdentity(queryResult2));
                                    }*/
                                    synchronized (iterateUsers) {
                                        iterateUsers.remove(userName);
                                        // specific
                                        stubPoiLinks.addAll(stubPoiLinksPerUser);
                                        stubLocalPoiLinks.addAll(stubLocalPoiLinksPerUser);
                                    }
                                } catch (SQLException e) {
                                    error(String.format("Unable to process dbUser = %s, dbServerURL = %s. Cause:%n%s", userName, dbServerUrl, e));

                                }
                            });
                        } catch (Exception e) {
//                            error(String.format("No SourceDbUser = %s, dbServer = %s", cdcUser, dbServerUrl));
                        }
                    });
                    info("Finish processing dbServer = " + dbServerUrl);
                }
                info("Remaining users: " + iterateUsers);
                System.out.println("STUB_POI:\t" + stubPoiLinks);
                System.out.println("STUB_LOCAL_POI:\t" + stubLocalPoiLinks);
            });
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            System.out.println(String.format("Time elapsed = %d s", TimeUnit.SECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS)));
        }
    }

    private static void info(String message) {
        System.out.println(String.format("%s\t%s", DATE_FORMAT.format(new Date()), message));
    }

    private static void error(String message) {
        System.err.println(String.format("%s\t%s", DATE_FORMAT.format(new Date()), message));
    }
}
