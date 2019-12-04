package oracle.checker;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import oracle.checker.consumers.GatewaysCounterpartCriteriaConsumer;
import oracle.checker.consumers.SpeedProfilesCriteriaConsumer;
import oracle.checker.readers.TxtUsersReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RdfDataChecker {
    private static final Logger LOGGER = LoggerFactory.getLogger(RdfDataChecker.class);
    private static final String USERS_QUERY = "select username from dba_users WHERE REGEXP_LIKE(username, '^CDCA_.*_%s$')";
    private static final String NO_SAMPLE_USERS = String.format("%s and not REGEXP_LIKE(username, '%s')", USERS_QUERY, TxtUsersReader.getSampleRegions());
    // when it's required to determine specific regions
    private static final String SPECIFIC_REGIONS = " and REGEXP_LIKE(username, '%s')";
    private static final String DB_DEFAULT_USER = "AKELA";
    private static final String DB_PASSWORD = "password";
    private static final String DB_SERVER_URL = "jdbc:oracle:thin:@akela-%s-%s-0%d.civof2bffmif.us-east-1.rds.amazonaws.com:1521:orcl";
    // TODO: put to build_config.properties or split by different files
    private static final Map<String, String> MARKET_TO_DVN = ImmutableMap.<String, String>builder()
//            .put("eu", "191G0")
//           .put("nar", "19122")
            .put("mrm", "191E3")
            .build();
    private static final  Set<Integer> DEFAULT_PROCESSED_SERVER_INDEXES = IntStream.rangeClosed(1, 8).boxed().collect(Collectors.toSet());

    private static void processTraversingUsers(Set<String> targetUsers, int ... processedServerIndexes) {
        MARKET_TO_DVN.forEach((market, dvn) ->
        {
            LOGGER.info(String.format("################## market = %s, dvn = %s ##################", market, dvn));
            Set<Integer> processedServers = processedServerIndexes.length == 0
                    ? DEFAULT_PROCESSED_SERVER_INDEXES
                    : Arrays.stream(processedServerIndexes).boxed().collect(Collectors.toSet());
            Predicate<String> isUseSpecificUsers = !targetUsers.isEmpty() ? targetUsers::contains : condition -> true;
            TxtUsersReader reader = new TxtUsersReader(market, dvn);
            Set<String> allUsers = reader.getCdcUsers();
//                Set<String> allUsers = reader.getSampleCdcUserWithDVN(dvn);
            Set<String> iterateUsers = new HashSet<>(allUsers);
//            SpeedProfilesCriteriaConsumer consumer = new SpeedProfilesCriteriaConsumer(market, dvn);
//            PoiStubCriteriaConsumer consumer = new PoiStubCriteriaConsumer();
//            BasicCriteriaConsumer consumer = new BasicCriteriaConsumer(GatewaysCriteria.REAL_LINKS_WITHOUT_GATEWAY_ON_BORDER);
            GatewaysCounterpartCriteriaConsumer consumer = new GatewaysCounterpartCriteriaConsumer(market);
            allUsers.stream().filter(isUseSpecificUsers).filter(iterateUsers::contains).forEach(cdcUser ->
            {
                for (int i : processedServers) {
                    String dbServerUrl = String.format(DB_SERVER_URL, market, dvn, i)
                            // exceptional case
                            .replaceAll("_", "-");
                    Set<String> dbServerUsers = new HashSet<>();
                    LOGGER.debug(String.format("Attempt to connect on %s with user = %s", dbServerUrl, cdcUser));
                    try (Connection connection = QueryTimeouter.getConnection(dbServerUrl, cdcUser, DB_PASSWORD);
                         ResultSet userNames = QueryTimeouter.getResultSet(connection, String.format(USERS_QUERY, dvn))) { // + NO_SAMPLE_USERS_PREDICATE
                        LOGGER.debug("Start processing dbServer = " + dbServerUrl);
                        while (userNames.next()) {
                            dbServerUsers.add(userNames.getString(1));
                        }
                        dbServerUsers = reader.withoutSampleUsers(dbServerUsers);
                        LOGGER.debug("Collected CDCA users: " + dbServerUsers);
                        // users
                        dbServerUsers.stream().filter(isUseSpecificUsers).forEach(userName ->
                        {
                            // Basic: e.g. POI -> new BasicCriteriaConsumer({StubbleCriteria.STUB_POI, StubbleCriteria.STUB_LOCAL_POI})
                            //                      .processDbUser(connection, userName, dbServerUrl);
                               /* new BasicCriteriaConsumer(StubbleCriteria.STUB_POI, StubbleCriteria.STUB_LOCAL_POI)
                                        .processDbUser(connection, userName, dbServerUrl);*/
                            // Specific - e.g. GatewaysCounterpartCriteriaConsumer
                            try
                            {
                                consumer.processDbUser(connection, userName, dbServerUrl);
                            }
                            catch (Exception e)
                            {
                                LOGGER.error(String.format("Error occurred while processing result set:" +
                                            " SourceDbUser = %s, dbServer = %s", userName, dbServerUrl), e);

                            }
                            finally {
                                synchronized (iterateUsers) {
                                    iterateUsers.remove(userName);
                                }
                            }
                        });
                    } catch (Exception e) {
                        if (e instanceof  TimeoutException)
                        {
                            synchronized (iterateUsers) {
                                iterateUsers.remove(cdcUser);
                            }
                            LOGGER.error(String.format("Timeout = 1 min is exceeded: SourceDbUser = %s, dbServer = %s", cdcUser, dbServerUrl));
                        }
                    }
                    if (!dbServerUsers.isEmpty()) {
                        processedServers.remove(i);
                        LOGGER.debug("Finish processing dbServer = " + dbServerUrl);
                        break;
                    }
                }
            });
            LOGGER.debug("Remaining users: " + iterateUsers);
            // speed profiles
              /*  consumer.printSpeedProfiles();
                consumer.exportToSq3();*/
//            consumer.exportProfilesUsage();
            // poi
           /*consumer.printDuplicatedPOI();
            consumer.printAll();
            consumer.printOddPoi();*/
           // counterparts
//           consumer.printGatewaysForRegion(market, "CDCA_GBR_E2_E6_191G0", Sets.newHashSet(341408, 341603, 398018));
            consumer.printGatewaysWithoutCounterPart();
            consumer.printGatewaysForRegion("CDCA_CHL_SA_191E3");
            consumer.printGatewaysForRegion("CDCA_CHL_NR_191E3");
        });
    }

    private static void processWithDefaultUser(Set<String> targetUsers, int ... processedServerIndexes) {
        MARKET_TO_DVN.forEach((market, dvn) ->
        {
            LOGGER.info(String.format("################## market = %s, dvn = %s ##################", market, dvn));
            TxtUsersReader reader = new TxtUsersReader(market, dvn);
            SpeedProfilesCriteriaConsumer consumer = new SpeedProfilesCriteriaConsumer(market, dvn);
            Set<Integer> processedIndexes = processedServerIndexes.length == 0
                    ? DEFAULT_PROCESSED_SERVER_INDEXES
                    : Arrays.stream(processedServerIndexes).boxed().collect(Collectors.toSet());
            Predicate<String> isUseSpecificUsers = !targetUsers.isEmpty() ? targetUsers::contains : condition -> true;
            for (int i : processedIndexes) {
                String dbServerUrl = String.format(DB_SERVER_URL, market, dvn, i)
                        // exceptional case
                        .replaceAll("_", "-");
                Set<String> dbServerUsers = new HashSet<>();
                LOGGER.debug("Start processing dbServer = " + dbServerUrl);
                try (Connection connection = QueryTimeouter.getConnection(dbServerUrl, DB_DEFAULT_USER, DB_PASSWORD);
                     ResultSet cdcUsers = QueryTimeouter.getResultSet(connection, String.format(USERS_QUERY, dvn))) { // + NO_SAMPLE_USERS_PREDICATE
                    LOGGER.debug("Connect to dbServer = " + dbServerUrl);
                    while (cdcUsers.next()) {
                        dbServerUsers.add(cdcUsers.getString(1));
                    }
                    dbServerUsers = reader.withoutSampleUsers(dbServerUsers);
                    LOGGER.debug("Collected CDCA users: " + dbServerUsers);
                    // users
                    dbServerUsers.stream().filter(isUseSpecificUsers).forEach(userName ->
                    {
                        // Basic: e.g. POI -> new BasicCriteriaConsumer({StubbleCriteria.STUB_POI, StubbleCriteria.STUB_LOCAL_POI})
                        //                      .processDbUser(connection, userName, dbServerUrl);
                               /* new BasicCriteriaConsumer(StubbleCriteria.STUB_POI, StubbleCriteria.STUB_LOCAL_POI)
                                        .processDbUser(connection, userName, dbServerUrl);*/
                        // Specific - e.g. GatewaysCounterpartCriteriaConsumer
                        consumer.processDbUser(connection, userName, dbServerUrl);
                    });
                } catch (Exception e) {
                    LOGGER.error(String.format("No SourceDbUser = %s, dbServer = %s. Cause: = %s", DB_DEFAULT_USER, dbServerUrl, e.toString()));
                }
                LOGGER.debug("Finish processing dbServer = " + dbServerUrl);
            }
//            consumer.printSpeedProfiles();
//            consumer.exportToSq3();
//            consumer.exportProfilesUsage();
        });
    }

    public static void main(String[] args) {
        long start = System.nanoTime();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            DriverManager.setLoginTimeout(60);
            Set<String> targetUsers = Sets.newHashSet("CDCA_CHL_SA_191E3", "CDCA_CHL_NR_191E3");
//            processTraversingUsers(targetUsers, 1, 8);
            processTraversingUsers(Collections.emptySet());
//          processWithDefaultUser();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            QueryTimeouter.shutdown();
            LOGGER.info(String.format("Time elapsed = %d s", TimeUnit.SECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS)));
        }
    }
}
