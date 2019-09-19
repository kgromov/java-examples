package oracle.signposts.consumers;

import oracle.signposts.criterias.StubbleCriteria;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PoiCriteriaConsumer implements ICriteriaConsumer {

    @Override
    public void processDbUser(String dbUser, String dbServerURL) {
        try (Connection connection = DriverManager.getConnection(dbServerURL, dbUser, "password");
             ResultSet stubbleLinks = connection.createStatement().executeQuery(StubbleCriteria.STUB_POI.getQuery());
             ResultSet stubLocalLinks = connection.createStatement().executeQuery(StubbleCriteria.STUB_LOCAL_POI.getQuery())) {

            if (stubbleLinks.next())
            {
                System.out.println(String.format("SourceDbUser = %s, dbServer = %s", dbUser, dbServerURL));
                System.out.println(StubbleCriteria.STUB_POI.getIdentity(stubbleLinks));
            }
            if (stubLocalLinks.next())
            {
                System.out.println(String.format("SourceDbUser = %s, dbServer = %s", dbUser, dbServerURL));
                System.out.println(StubbleCriteria.STUB_LOCAL_POI.getIdentity(stubLocalLinks));
            }
        } catch (SQLException e) {
            System.out.println(String.format("Unable to process dbUser = %s, dbServerURL = %s. Cause:%n%s", dbUser, dbServerURL, e));
        }
    }

    @Override
    public void processDbUser(Connection connection, String dbUser, String dbServerURL) {
        try (ResultSet stubbleLinks = connection.createStatement().executeQuery(StubbleCriteria.STUB_POI.getQuery());
             ResultSet stubLocalLinks = connection.createStatement().executeQuery(StubbleCriteria.STUB_LOCAL_POI.getQuery())) {

            if (stubbleLinks.next())
            {
                System.out.println(String.format("SourceDbUser = %s, dbServer = %s", dbUser, dbServerURL));
                System.out.println(StubbleCriteria.STUB_POI.getIdentity(stubbleLinks));
            }
            if (stubLocalLinks.next())
            {
                System.out.println(String.format("SourceDbUser = %s, dbServer = %s", dbUser, dbServerURL));
                System.out.println(StubbleCriteria.STUB_LOCAL_POI.getIdentity(stubLocalLinks));
            }
        } catch (SQLException e) {
            System.out.println(String.format("Unable to process dbUser = %s, dbServerURL = %s. Cause:%n%s", dbUser, dbServerURL, e));
        }
    }
}
