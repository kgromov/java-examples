package db;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Arrays;

/**
 * Created by konstantin on 08.01.2017.
 */
public class TestDB {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/test";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "admin";

    public static String quote(String value) {
        return String.format("'%s'", value);
    }

    public static void main(String[] args) throws Exception {
        MySqlDB mySqlDB = new MySqlDB("test", "root", "admin");
        /*for (int i = 0; i < Integer.MAX_VALUE; i++) {
            mySqlDB.insert("new_table", Arrays.asList("idnew_table", "first_name", "last_name", "email", "state", "zipcode"),
                    Arrays.asList(String.valueOf(i),
                            quote(RandomStringUtils.randomAlphabetic(19)),
                            quote(RandomStringUtils.randomAlphabetic(19)),
                            quote(RandomStringUtils.random(19, true, true)),
                            quote(RandomStringUtils.randomAlphabetic(2)),
                            quote(RandomStringUtils.randomNumeric(5))
                    ));
        }*/
        mySqlDB.printTable("new_table", Arrays.asList("idnew_table", "first_name", "last_name", "email", "state", "zipcode"));
        mySqlDB.closeConnection();
    }
}
