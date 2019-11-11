package oracle.checker.consumers;

import oracle.checker.criterias.ICriteria;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

public class BasicCriteriaConsumer implements ICriteriaConsumer {
    private ICriteria[] criterias;

    public BasicCriteriaConsumer(ICriteria... criterias) {
        this.criterias = criterias;
    }

    @Override
    public void processDbUser(String dbUser, String dbServerURL) {

    }

    @Override
    public void processDbUser(Connection connection, String dbUser, String dbServerURL) {
        for (ICriteria criteria : criterias)
        {
            try (ResultSet resultSet = connection.createStatement().executeQuery(ICriteria.getQuery(criteria.getQuery(), dbUser))) {
                Set<String> results = new LinkedHashSet<>();
                while (resultSet.next())
                if (resultSet.next()) {
                    results.add(criteria.getIdentity(resultSet));
                }
                if (!results.isEmpty())
                {
                    LOGGER.info(String.format("SourceDbUser = %s, dbServer = %s", dbUser, dbServerURL));
                    LOGGER.info(results.toString());
                }
            } catch (SQLException e) {
                LOGGER.error(String.format("Unable to process dbUser = %s, dbServerURL = %s. Cause:%n%s", dbUser, dbServerURL, e));
            }
        }
    }
}
