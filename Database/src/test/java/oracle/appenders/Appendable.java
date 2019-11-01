package oracle.appenders;

import java.sql.Connection;
import java.sql.PreparedStatement;

public interface Appendable<T> {

    void append(PreparedStatement statement, T object);

    void append(Connection connection, String query, T object);
}
