package jenkins;

import com.google.common.collect.ImmutableMap;
import jenkins.forkjoinpool.BuildInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by konstantin on 25.03.2018.
 */
// TODO: made more generic; this one move to HTML module
public class Convertor {
    // sql
    private static final String DB_URI_PREFIX = "jdbc:sqlite:file:";
    private static final String TABLE_NAME = "RunSummary";
    private static final SimpleDateFormat BUILD_START_DATE = new SimpleDateFormat("dd-MM-yyyy");

    private static final String JOB_NAME_COLUMN = "JobName";
    private static final String DISPLAY_NAME_COLUMN = "DisplayName";
    private static final String BUILD_NUMBER_COLUMN = "BuildNumber";
    private static final String PARAMETERS_COLUMN = "Parameters";
    private static final String LOG_SIZE_COLUMN = "LogSize";
    private static final String BUILD_TIME_COLUMN = "BuildTime";
    private static final String BUILD_RESULT_COLUMN = "BuildResult";
    private static final String PATH_COLUMN = "PathToResult";
    private static final String CDC_USER = "CdcUser";
    private static final String KEEP_USER = "KeepUser";
    private static final String DB_SERVER = "DbServer";

    private static final String TEXT_TYPE = "TEXT";
    private static final String INTEGER_TYPE = "INTEGER";
    private static final String REAL_TYPE = "REAL";

    private static final Map<String, String> COLUMN_TYPE_BY_COLUMN_NAME = ImmutableMap.<String, String>builder()
            .put(JOB_NAME_COLUMN, TEXT_TYPE)
            .put(DISPLAY_NAME_COLUMN, TEXT_TYPE)
            .put(BUILD_NUMBER_COLUMN, INTEGER_TYPE)
            .put(PARAMETERS_COLUMN, TEXT_TYPE)
            .put(LOG_SIZE_COLUMN, REAL_TYPE)
            .put(BUILD_TIME_COLUMN, INTEGER_TYPE)
            .put(BUILD_RESULT_COLUMN, TEXT_TYPE)
            .put(PATH_COLUMN, TEXT_TYPE)
            .put(CDC_USER, TEXT_TYPE)
            .put(KEEP_USER, TEXT_TYPE)
            .put(DB_SERVER, TEXT_TYPE)
            .build();

    private Convertor() {
    }

    private static void convertToSqLite(String xmlFilePath, String dbFilePath) {
        try {
            File xmlFile = new File(xmlFilePath);
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = dBuilder.parse(xmlFile);

            NodeList builds = document.getElementsByTagName("Build");
            if (builds.getLength() > 0) {
                createTable(dbFilePath);
                writeToSqLite(dbFilePath, builds);
            }
            // compare speed
           /* XPathFactory xpathfactory = XPathFactory.newInstance();
            XPath xpath = xpathfactory.newXPath();
            XPathExpression expr = xpath.compile("//Build");

            NodeList attrNodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);*/

        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + e);
        }
    }

    public static void convertToSqLite(Settings settings, BuildInfo upstreamBuild) {
        try {
            Path inputFilePath = Paths.get(settings.getOutputDir(), "index.xml");
            Path outputFolder = Paths.get(settings.getDbOutputDir(),
                    settings.getDvn(), "connections", BUILD_START_DATE.format(new Date(upstreamBuild.getTimestamp())));
            Files.createDirectories(outputFolder);
            Path outputFilePath = outputFolder.resolve(settings.getJobName() + "-" + settings.getBuildNumber() + ".sq3");
//            Path outputFilePath = outputFolder.resolve(upstreamBuild.getBuildName() + ".sq3");
            Files.deleteIfExists(outputFilePath);
            Files.createFile(outputFilePath);

            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = dBuilder.parse(inputFilePath.toFile());

            NodeList builds = document.getElementsByTagName("Build");
            if (builds.getLength() > 0) {
                createTable(outputFilePath.toString());
                writeToSqLite(outputFilePath.toString(), builds);
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.err.println("UsersXML: Error trying to instantiate DocumentBuilder " + e);
        }
    }

    private static String getCreateQuery() {
        return "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME +
                COLUMN_TYPE_BY_COLUMN_NAME.entrySet().stream()
                        .map(columnData -> columnData.getKey() + " " + columnData.getValue())
                        .collect(Collectors.joining(",", " (", ")"));
    }

    private static String getInsertQuery() {
        return "INSERT INTO " +
                TABLE_NAME +
                COLUMN_TYPE_BY_COLUMN_NAME.keySet().stream()
                        .collect(Collectors.joining(",", " (", ") ")) +
                COLUMN_TYPE_BY_COLUMN_NAME.entrySet().stream()
                        .map(columnData -> "?")
                        .collect(Collectors.joining(",", "VALUES (", ") "));
    }

    private static void createTable(String dbFilePath) {
        try (Connection connection = DriverManager.getConnection(DB_URI_PREFIX + dbFilePath);
             Statement statement = connection.createStatement()) {
            statement.execute(getCreateQuery());
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Unable to create table %s on dbFile - %s", TABLE_NAME, dbFilePath), e);
        }
    }

    private static void writeToSqLite(String dbFilePath, NodeList builds) {
        try (Connection connection = DriverManager.getConnection(DB_URI_PREFIX + dbFilePath);
             PreparedStatement statement = connection.prepareStatement(getInsertQuery())) {
            // start inserting
            connection.setAutoCommit(false);

            for (int i = 0; i < builds.getLength(); i++) {
                Element build = (Element) builds.item(i);
                if (build.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                String name = build.getElementsByTagName("Name").item(0).getTextContent();
                String jobName = build.getElementsByTagName("JobName").item(0).getTextContent();
                int number = Integer.parseInt(build.getElementsByTagName("Number").item(0).getTextContent());
                String parameters = build.getElementsByTagName("Parameters").item(0).getTextContent();
                float logSize = Float.parseFloat(build.getElementsByTagName("LogSize").item(0).getTextContent());
                int buildTime = Integer.parseInt(build.getElementsByTagName("BuildTime").item(0).getTextContent());
                String buildResult = build.getElementsByTagName("BuildResult").item(0).getTextContent();
                String pathToResult = build.getElementsByTagName("PathToResult").item(0).getTextContent();
                // credentials
                String cdcUser = null, keepUser = null, dbServer = null;
                if (build.getElementsByTagName("CdcUser").getLength() > 0) {
                    cdcUser = build.getElementsByTagName("CdcUser").item(0).getTextContent();
                    keepUser = build.getElementsByTagName("KeepUser").item(0).getTextContent();
                    dbServer = build.getElementsByTagName("DbServer").item(0).getTextContent();
                }

                int columnIndex = 0;
                statement.setString(++columnIndex, name);
                statement.setString(++columnIndex, jobName);
                statement.setInt(++columnIndex, number);
                statement.setString(++columnIndex, parameters);
                statement.setFloat(++columnIndex, logSize);
//                statement.setObject(++columnIndex, logSize, JDBCType.REAL);
                statement.setInt(++columnIndex, buildTime);
                statement.setString(++columnIndex, buildResult);
                statement.setString(++columnIndex, pathToResult);
                statement.setString(++columnIndex, cdcUser);
                statement.setString(++columnIndex, keepUser);
                statement.setString(++columnIndex, dbServer);
                statement.addBatch();
            }
            statement.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String folder = "C:\\Projects\\java-examples\\HTML\\target\\jenkins-builds\\";
        String xmlFilePath = folder + "index.xml";
        String dbFilePath = folder + "result.sq3";
        convertToSqLite(xmlFilePath, dbFilePath);
    }

}
