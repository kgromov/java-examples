package oracle.signposts.consumers;

import oracle.signposts.criterias.ICriteria;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BasicCriteriaConsumer implements ICriteriaConsumer {
    private ICriteria[] iCriterias;

    public BasicCriteriaConsumer(ICriteria... iCriterias) {
        this.iCriterias = iCriterias;
    }

    @Override
    public void processDbUser(String dbUser, String dbServerURL) {

    }

    @Override
    public void processDbUser(Connection connection, String dbUser, String dbServerURL) {
        for (ICriteria criteria : iCriterias)
        {
            try (ResultSet resultSet = connection.createStatement().executeQuery(criteria.getQuery())) {
                if (resultSet.next()) {
                    LOGGER.info(String.format("SourceDbUser = %s, dbServer = %s", dbUser, dbServerURL));
                    LOGGER.info( criteria.getIdentity(resultSet));
                }
            } catch (SQLException e) {
                LOGGER.severe(String.format("Unable to process dbUser = %s, dbServerURL = %s. Cause:%n%s", dbUser, dbServerURL, e));
            }
        }
    }
}
