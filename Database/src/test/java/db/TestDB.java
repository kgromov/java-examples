package db;

import dao.CsvDataProvider;
import org.apache.commons.lang3.StringUtils;
import sun.net.spi.nameservice.dns.DNSNameServiceDescriptor;

import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;

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
        String str = "Hello world";
        Set<String> set= Arrays.stream(str.split(" ")).collect(Collectors.toSet());
        set.remove("world");
        ServiceLoader.load(DNSNameServiceDescriptor.class).forEach(clazz -> System.out.println(clazz));
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
//        mySqlDB.printTable("new_table", Arrays.asList("idnew_table", "first_name", "last_name", "email", "state", "zipcode"));
        String tableName = "movie_ratings";
        List<String> headers = Arrays.asList("movie_id", "user_id", "rating", "time_stamp");
        List<List<String>> data = CsvDataProvider.getData("D:\\course_materials\\ml-100k\\ml-100k\\u.data", '\t');
        long start = System.currentTimeMillis();
       /* data.forEach(row -> mySqlDB.insert(tableName, headers, row));
        System.out.println(String.format("Time to execute %d inserts via Statement with DML query direclty = %d ms", data.size(), System.currentTimeMillis() - start));*/

    /*    // prepared statement
        String insertQuery = String.format("INSERT INTO %s.%s (%s) VALUES (%s)", "test",
                tableName, StringUtils.join(headers, ","), headers.stream().map(header -> "?").collect(Collectors.joining(",")));
        start = System.currentTimeMillis();
        PreparedStatement statement = mySqlDB.getConnection().prepareStatement(insertQuery);
        data.forEach(row -> mySqlDB.insertWithPreparedStatement(statement, row));
        System.out.println(String.format("Time to execute %d inserts via PreparedStatement with DML = %d ms", data.size(), System.currentTimeMillis() - start));
        statement.close();*/

        // prepared statement with batch
        String insertQuery = String.format("INSERT INTO %s.%s (%s) VALUES (%s)", "test",
                tableName, StringUtils.join(headers, ","), headers.stream().map(header -> "?").collect(Collectors.joining(",")));
        start = System.currentTimeMillis();
        PreparedStatement statement2 = mySqlDB.getConnection().prepareStatement(insertQuery);
        data.forEach(row -> mySqlDB.insertWithPreparedStatementAndBatch(statement2, row));
        statement2.executeBatch();
        System.out.println(String.format("Time to execute %d inserts via PreparedStatement with DML and batch = %d ms", data.size(), System.currentTimeMillis() - start));
        statement2.close();

        mySqlDB.closeConnection();
    }
}
