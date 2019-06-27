package oracle.signposts;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ICriteria {

    default String getIdentity(ResultSet resultSet) throws SQLException
    {
        return resultSet.getObject(1).toString();
    }

    String getQuery();
}
