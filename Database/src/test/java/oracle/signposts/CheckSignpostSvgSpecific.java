package oracle.signposts;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class CheckSignpostSvgSpecific {
    private static final String STUB_LINKS_QUERY = "select username from dba_users WHERE username like 'CDCA_%'";
    private static final String SIGNPOST_BLOB_QUERY = "select FILE_ID from CDC_RDF_FILE_2D_SIGN " +
            "where REGEXP_LIKE (utl_raw.cast_to_varchar2(dbms_lob.substr(FILE_OBJECT, 2000, 2001)), '(dx)|(dy)')";
    private static final String USER_NAME = "CDCA_RUS_4C_1_191E0";
    private static final String DB_SERVER_URL = "jdbc:oracle:thin:@akela-eu-191e0-02.civof2bffmif.us-east-1.rds.amazonaws.com:1521:orcl";

    public static void main(String[] args) {
        long start = System.nanoTime();
        try {
            Set<String> users = new HashSet<>();
            Class.forName("oracle.jdbc.driver.OracleDriver");
            // init source link gateways
            try (Connection connection = DriverManager.getConnection(DB_SERVER_URL, USER_NAME, "password");
                 ResultSet userNames = connection.createStatement().executeQuery(STUB_LINKS_QUERY);
                 ResultSet signposts = connection.createStatement().executeQuery(SIGNPOST_BLOB_QUERY)) {
                System.out.println("SourceDbUser " + USER_NAME);
                while (userNames.next()) {
                    users.add(userNames.getString(1));
                }
                if (signposts.next()) {
                    System.out.println("dx/dy are found! E.g. :" + signposts.getObject(1));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // users
            users.parallelStream().forEach(userName ->
            {
                try (Connection connection = DriverManager.getConnection(DB_SERVER_URL, userName, "password");
                     ResultSet signposts = connection.createStatement().executeQuery(SIGNPOST_BLOB_QUERY)) {
                    System.out.println("SourceDbUser " + userName);
                    if (signposts.next()) {
                        System.out.println(signposts.getObject(1));
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            System.out.println(String.format("Time elapsed = %d ms", TimeUnit.MILLISECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS)));
        }
    }
}
