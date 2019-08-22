package oracle.signposts.consumers;

import java.sql.Connection;

public interface ICriteriaConsumer {

    void processDbUser(String dbUser, String dbServerURL);

    void processDbUser(Connection connection, String dbUser, String dbServerURL);
}
