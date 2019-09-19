package oracle.signposts;

import com.google.common.collect.ImmutableMap;
import oracle.signposts.consumers.GatewaysCounterpartCriteriaConsumer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class CheckSignpostSvg {
    private static final Logger LOGGER = Logger.getLogger(CheckSignpostSvg.class.getName());
    private static final String USERS_QUERY = "select username from dba_users WHERE REGEXP_LIKE(username, '^CDCA_.*_%s$')";
    private static final String NO_SAMPLE_USERS =  String.format("%s and not REGEXP_LIKE(username, '%s')", USERS_QUERY, UsersReader.getSampleRegions());
    private static final String DB_PASSWORD = "password";
    private static final String DB_SERVER_URL = "jdbc:oracle:thin:@akela-%s-%s-0%d.civof2bffmif.us-east-1.rds.amazonaws.com:1521:orcl";
    private static final Map<String, String> MARKET_TO_DVN = ImmutableMap.<String, String>builder()
            .put("eu", "191F0")
//            .put("nar", "191F0")
//            .put("mrm", "191E3")
            .build();

    public static void main(String[] args) {
        long start = System.nanoTime();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
//            ICriteria criteria = GatewaysCriteria.ADMIN_WIDE_REGULATIONS;
//            ICriteria criteria = StubbleCriteria.STUB_LOCAL_POI;
            MARKET_TO_DVN.forEach((market, dvn) ->
            {
                LOGGER.info(String.format("################## market = %s, dvn = %s ##################", market, dvn));
                UsersReader reader = new UsersReader(market, dvn);
                Set<String> allUsers = reader.getCdcUsers();
//                Set<String> allUsers = reader.getSampleCdcUserWithDVN(dvn);
                Set<String> iterateUsers = new HashSet<>(allUsers);
                GatewaysCounterpartCriteriaConsumer consumer = new GatewaysCounterpartCriteriaConsumer();
                for (int i = 1; i < 9; i++) {
                    String dbServerUrl = String.format(DB_SERVER_URL, market, dvn, i)
                            // exceptional case
                            .replaceAll("_", "-");
                    LOGGER.info("Start processing dbServer = " + dbServerUrl);
                    allUsers.stream().filter(iterateUsers::contains).forEach(cdcUser ->
                    {
                        Set<String> dbServerUsers = new HashSet<>();
                        try (Connection connection = DriverManager.getConnection(dbServerUrl, cdcUser, DB_PASSWORD);
                             ResultSet userNames = connection.createStatement().executeQuery(String.format(USERS_QUERY, dvn))) { // + NO_SAMPLE_USERS_PREDICATE
                            while (userNames.next()) {
                                dbServerUsers.add(userNames.getString(1));
                            }
                            dbServerUsers = reader.withoutSampleUsers(dbServerUsers);
                            // users
                            dbServerUsers.forEach(userName ->
                            {
                             /*   try (Connection connection2 = DriverManager.getConnection(dbServerUrl, userName, DB_PASSWORD);
                                     ResultSet queryResult2 = connection2.createStatement().executeQuery(criteria.getQuery())) {
                                    if (queryResult2.next()) {
                                        LOGGER.info(String.format("SourceDbUser = %s, dbServer = %s", userName, dbServerUrl));
                                        LOGGER.info("Criteria is found! E.g. :" + criteria.getIdentity(queryResult2));
                                    }
                                    synchronized (iterateUsers) {
                                        iterateUsers.remove(userName);
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }*/
                                  // POI
//                                new PoiCriteriaConsumer().processDbUser(userName, dbServerUrl);

                                // Counterparts
                                consumer.processDbUser(connection, userName, dbServerUrl);

                                synchronized (iterateUsers) {
                                    iterateUsers.remove(userName);
                                }
                            });
                        } catch (Exception e) {
//                            System.err.println(String.format("No SourceDbUser = %s, dbServer = %s", cdcUser, dbServerUrl));
                        }
                    });
                    LOGGER.info("Finish processing dbServer = " + dbServerUrl);
                }
                LOGGER.info("Remaining users: " + iterateUsers);
                consumer.printGatewaysWithoutCounterPart();
            });
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            LOGGER.info(String.format("Time elapsed = %d s", TimeUnit.SECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS)));
        }
    }
}
