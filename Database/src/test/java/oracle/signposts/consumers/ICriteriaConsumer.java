package oracle.signposts.consumers;

import java.sql.Connection;
import java.util.logging.Logger;

public interface ICriteriaConsumer {
    // TODO: replace with sl4j logger
    Logger LOGGER = Logger.getLogger(ICriteriaConsumer.class.getName());

    @Deprecated
    void processDbUser(String dbUser, String dbServerURL);

    void processDbUser(Connection connection, String dbUser, String dbServerURL);
}
