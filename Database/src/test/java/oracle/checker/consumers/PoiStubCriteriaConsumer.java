package oracle.checker.consumers;

import com.google.common.collect.Sets;
import oracle.checker.criterias.ICriteria;
import oracle.checker.criterias.StubbleCriteria;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class PoiStubCriteriaConsumer implements ICriteriaConsumer {
    private Map<String, Set<String>> stubPOIs = new TreeMap<>();
    private Map<String, Set<Integer>> stubPoiCategories = new TreeMap<>();

    private Map<Integer, Set<String>> localPoiPerRegion = new TreeMap<>();
    private Map<Integer, Set<String>> stubPoiPerRegion = new TreeMap<>();
    private Map<Integer, Set<Integer>> localLinksPerCatId = new TreeMap<>();
    private Map<Integer, Set<Integer>> stubLinksPerCatId = new TreeMap<>();

    @Override
    public void processDbUser(String dbUser, String dbServerURL) {
        throw new UnsupportedOperationException("Method is deprecated! Use processDbUser(Connection connection, String dbUser, String dbServerURL)");
    }

    @Override
    public void processDbUser(Connection connection, String dbUser, String dbServerURL) {
       /* ICriteria criteria = StubbleCriteria.STUB_POI;
        try (ResultSet resultSet = connection.createStatement().executeQuery(criteria.getQuery(dbUser))) {
            while (resultSet.next()) {
                stubPOIs.computeIfAbsent(dbUser, poi -> new HashSet<>()).add(criteria.getIdentity(resultSet));
                stubPoiCategories.computeIfAbsent(dbUser, categories -> new TreeSet<>()).add(resultSet.getInt("CAT_ID"));
            }
        } catch (SQLException e) {
            System.err.println(String.format("Unable to process dbUser = %s, dbServerURL = %s, query = %s. Cause:%n%s",
                    dbUser, dbServerURL, criteria.getQuery(dbUser), e));
        }*/
        ICriteria stubPoi = StubbleCriteria.STUB_POI_AIRPORT;
        try (Statement statement = connection.createStatement()) {
            statement.setFetchSize(DEFAULT_FETCH_SIZE);
            ResultSet resultSet = statement.executeQuery(stubPoi.getQuery(dbUser));
            while (resultSet.next()) {
                int linkId = resultSet.getInt(1);
//                int catId = resultSet.getInt(4);
                stubPoiPerRegion.computeIfAbsent(linkId, regions -> new TreeSet<>()).add(dbUser);
                stubLinksPerCatId.computeIfAbsent(4581, links -> new HashSet<>()).add(linkId);
            }
            resultSet.close();
        } catch (SQLException e) {
            System.err.println(String.format("Unable to process dbUser = %s, dbServerURL = %s, query = %s. Cause:%n%s",
                    dbUser, dbServerURL, stubPoi.getQuery(dbUser), e));
        }

        ICriteria localPoi = StubbleCriteria.STUB_LOCAL_POI_AIRPORT;
        try (Statement statement = connection.createStatement()) {
            statement.setFetchSize(DEFAULT_FETCH_SIZE);
            ResultSet resultSet = statement.executeQuery(localPoi.getQuery(dbUser));
            while (resultSet.next()) {
                int linkId = resultSet.getInt(1);
//                int catId = resultSet.getInt(4);
                localPoiPerRegion.computeIfAbsent(linkId, regions -> new TreeSet<>()).add(dbUser);
                localLinksPerCatId.computeIfAbsent(4581, links -> new HashSet<>()).add(linkId);
            }
            resultSet.close();
        } catch (SQLException e) {
            System.err.println(String.format("Unable to process dbUser = %s, dbServerURL = %s, query = %s. Cause:%n%s",
                    dbUser, dbServerURL, localPoi.getQuery(dbUser), e));
        }
    }

    public void printPOI() {
        System.out.println("STUB POI:\n" + stubPOIs);
        System.out.println("POI categories:\n" + stubPoiCategories);
    }

    public void printAll()
    {
        System.out.println("STUB_POI_LINKS:\n"+ stubPoiPerRegion);
        System.out.println("STUB_POI_LOCAL_LINKS:\n"+ localPoiPerRegion);
    }

    public void printOddPoi()
    {
        System.out.println("Odd links:");
        Set<Integer> catIdStubLinks = stubLinksPerCatId.keySet();
        Set<Integer> catIdLocalLinks = localLinksPerCatId.keySet();
        // missed whole links group by categoryId
        System.out.println(String.format("Missed CAT_IDs = %s for local links (up to stub)", Sets.difference(catIdStubLinks, catIdLocalLinks)));
        System.out.println(String.format("Missed CAT_IDs = %s for stub links (up to local)", Sets.difference(catIdLocalLinks, catIdStubLinks)));
        // difference in linkIDs by categoryId
        System.out.println("Difference in linkIDs by categoryId");
        String balancedTemplate = "Balanced stub/stub local link by POI: CAT_ID = %d, same = %d";
        String notBalancedTemplate = "Not balanced stub/stub local link by POI: CAT_ID = %d, same = %d, stubDiff = %d, localDiff = %d";
        Sets.intersection(catIdStubLinks, catIdLocalLinks).forEach(catId ->
        {
            Set<Integer> stubLinksByCategory = stubLinksPerCatId.get(catId);
            Set<Integer> localLinksByCategory = localLinksPerCatId.get(catId);

            int sameLinksAmount = Sets.intersection(stubLinksByCategory, localLinksByCategory).size();
            int diffStubLinkAmount = Sets.difference(stubLinksByCategory, localLinksByCategory).size();
            int diffLocalLinksAmount = Sets.difference(localLinksByCategory, stubLinksByCategory).size();

            if (diffLocalLinksAmount == 0 && diffStubLinkAmount == diffLocalLinksAmount)
            {
                System.out.println(String.format(balancedTemplate, catId, sameLinksAmount));
            }
            else
            {
                System.out.println(String.format(notBalancedTemplate, catId, sameLinksAmount, diffStubLinkAmount, diffLocalLinksAmount));
            }
        });
    }
}
