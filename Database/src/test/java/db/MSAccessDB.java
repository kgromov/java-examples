package db;

import java.io.File;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by konstantin on 08.01.2017.
 */
public class MSAccessDB extends DataBase {

    public MSAccessDB(String pathToDB) {
        File dataBase = new File(pathToDB);
        db = dataBase.getName();
        connectionString = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)}; DBQ=" + dataBase.getAbsolutePath();
        connectionString = "jdbc:ucanaccess://" + dataBase.getAbsolutePath();
        createConnection();
    }

    @Override
    protected void createConnection() {
        try {
            logger.info("Create connection for MS Access Driver");
           /* Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            );*/
            connection = DriverManager.getConnection(connectionString);
            statement = connection.createStatement();
        } catch (SQLException e) {
            logger.severe("Unable to create connection for MS Access Driver cause of\n" + e.getMessage());
            throw new RuntimeException("Unable to create connection for MS Access Driver cause of\n" + e.getMessage());
        }
    }

    public void testConnection() {
//        ResultSet resultSet = executeQuery("SELECT * FROM MSysObjects WHERE MSysObjects.Type=1");
        ResultSet resultSet = executeQuery("SELECT * FROM 10_10_32_62_nnm_main_1");
        try {
            while (resultSet.next()) {
                logger.info(resultSet.getString(1));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}