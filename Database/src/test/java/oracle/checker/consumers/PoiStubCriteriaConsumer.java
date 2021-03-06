package oracle.checker.consumers;

import com.google.common.collect.Sets;
import oracle.checker.criterias.ICriteria;
import oracle.checker.criterias.StubbleCriteria;
import org.apache.commons.lang3.tuple.Pair;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class PoiStubCriteriaConsumer implements ICriteriaConsumer {
    private Map<String, Set<String>> stubPOIs = new TreeMap<>();
    private Map<String, Set<Integer>> stubPoiCategories = new TreeMap<>();

    private Map<Pair<Integer, Integer>, Set<String>> localPoiPerRegion = new TreeMap<>();
    private Map<Pair<Integer, Integer>, Set<String>> stubPoiPerRegion = new TreeMap<>();
    private Set<Pair<Integer, Integer>> localLinks = new HashSet<>();
    private Set<Pair<Integer, Integer>> stubLinks = new HashSet<>();

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
        try (ResultSet resultSet = connection.createStatement().executeQuery(stubPoi.getQuery(dbUser))) {
            while (resultSet.next()) {
                int linkId = resultSet.getInt(1);
                int poiId = resultSet.getInt(2);
                stubPoiPerRegion.computeIfAbsent(Pair.of(linkId, poiId), regions -> new TreeSet<>()).add(dbUser);
                stubLinks.add(Pair.of(linkId, poiId));
            }
        } catch (SQLException e) {
            System.err.println(String.format("Unable to process dbUser = %s, dbServerURL = %s, query = %s. Cause:%n%s",
                    dbUser, dbServerURL, stubPoi.getQuery(dbUser), e));
        }

        ICriteria localPoi = StubbleCriteria.STUB_LOCAL_POI_AIRPORT;
        try (ResultSet resultSet = connection.createStatement().executeQuery(localPoi.getQuery(dbUser))) {
            while (resultSet.next()) {
                int linkId = resultSet.getInt(1);
                int poiId = resultSet.getInt(2);
                localPoiPerRegion.computeIfAbsent(Pair.of(linkId, poiId), regions -> new TreeSet<>()).add(dbUser);
                localLinks.add(Pair.of(linkId, poiId));
            }
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

    public void printDuplicatedPOI()
    {
        Sets.intersection(stubLinks, localLinks).forEach(pair ->
                System.out.println(String.format("LINK_ID = %d, POI_ID = %d, STUB_REGIONS = %s, REAL_REGIONS = %s",
                        pair.getLeft(), pair.getRight(), stubPoiPerRegion.get(pair), localPoiPerRegion.get(pair)))
        );
    }

    public void printOddPoi()
    {
        System.out.println("Odd links:");
        Sets.difference(localLinks, stubLinks).forEach(linkId ->
                System.out.println(String.format("REGION = %s, LINK_ID = %d has no stub counterpart",
                        localPoiPerRegion.get(linkId), linkId)));

        Sets.difference(stubLinks, localLinks).forEach(linkId ->
                System.out.println(String.format("REGION = %s, LINK_ID = %d has no stub local counterpart",
                        stubPoiPerRegion.get(linkId), linkId)));
    }
}
