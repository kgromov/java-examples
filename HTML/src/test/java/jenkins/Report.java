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
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by konstantin on 25.03.2018.
 */
public class Report {
    private static final String TEMPLATES_DIR = "C:\\Projects\\java-examples\\HTML\\src\\main\\resources\\templates";
    private static final String OUTPUT_DIR = "C:\\Projects\\java-examples\\HTML\\target\\jenkins-builds";
    private static final String OUTPUT_XML = OUTPUT_DIR + "\\index.xml";
    // sql
    private static final String DB_URI_PREFIX = "jdbc:sqlite:file:";
    private static final String OUTPUT_SQ3 = OUTPUT_DIR + "\\result.sq3";
    private static final String TABLE_NAME = "RunSummary";

    private static final String JOB_NAME_COLUMN = "JobName";
    private static final String DISPLAY_NAME_COLUMN = "DisplayName";
    private static final String BUILD_NUMBER_COLUMN = "BuildNumber";
    private static final String PARAMETERS_COLUMN = "Parameters";
    private static final String LOG_SIZE_COLUMN = "LogSize, KB";
    private static final String BUILD_TIME_COLUMN = "BuildTime, sec";
    private static final String BUILD_RESULT_COLUMN = "BuildResult";
    private static final String PATH_COLUMN = "PathToResult";
    private static final String STACK_TRACE_COLUMN = "StackTrace";

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
            .put(STACK_TRACE_COLUMN, TEXT_TYPE)
            .build();

    private Report() {
    }

    public static void convertToSqLite() {
        try {
            File xmlFile = new File(OUTPUT_XML);
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = dBuilder.parse(xmlFile);

            NodeList builds = document.getElementsByTagName("Build");
            if (builds.getLength() > 0)
            {
                writeToSqLite(builds);
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

    private static String getCreateQuery()
    {
        return "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME +
                COLUMN_TYPE_BY_COLUMN_NAME.entrySet().stream()
                        .map(columnData -> columnData.getKey() + " " + columnData.getValue())
                        .collect(Collectors.joining(",", " (", ")"));
    }

    private static String getInsertQuery()
    {
        return "INSERT INTO " +
                TABLE_NAME +
                COLUMN_TYPE_BY_COLUMN_NAME.keySet().stream()
                        .collect(Collectors.joining(",", " (", ") ")) +
                COLUMN_TYPE_BY_COLUMN_NAME.entrySet().stream()
                        .map(columnData -> "?")
                        .collect(Collectors.joining(",", "VALUES (", ") "));
    }

    private static void writeToSqLite(NodeList builds) {
        try (Connection connection = DriverManager.getConnection(DB_URI_PREFIX + OUTPUT_SQ3);
             Statement createStatement = connection.createStatement();
             PreparedStatement statement = connection.prepareStatement(getInsertQuery()))
        {
            // create table
            statement.execute(getCreateQuery());
            // start inserting
            connection.setAutoCommit(false);

            for (int i = 0; i < builds.getLength(); i++) {
                Element build = (Element) builds.item(i);
                String name = build.getElementsByTagName("Name").item(0).getTextContent();
                String jobName = build.getElementsByTagName("JobName").item(0).getTextContent();
                int number = Integer.parseInt(build.getElementsByTagName("Number").item(0).getTextContent());
                String parameters = build.getElementsByTagName("Parameters").item(0).getTextContent();
                float logSize = Float.parseFloat(build.getElementsByTagName("LogSize").item(0).getTextContent());
                int buildTime = Integer.parseInt(build.getElementsByTagName("BuildTime").item(0).getTextContent());
                String stackTrace = build.getElementsByTagName("StackTrace").item(0).getTextContent();
                String buildResult = build.getElementsByTagName("BuildResult").item(0).getTextContent();
                String pathToResult = build.getElementsByTagName("PathToResult").item(0).getTextContent();

                statement.setString(++i, name);
                statement.setString(++i, jobName);
                statement.setInt(++i, number);
                statement.setString(++i, parameters);
                statement.setFloat(++i, logSize);
//                statement.setObject(++i, logSize, JDBCType.REAL);
                statement.setInt(++i, buildTime);
                statement.setString(++i, stackTrace);
                statement.setString(++i, buildResult);
                statement.setString(++i, pathToResult);
                statement.addBatch();
            }
            createStatement.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // first: create root and than append all build to root -> <Builds>
    public static void writeJobsInfoToXml(BuildInfo rootJob) {
        // write root info
        writeToReport(rootJob);
        // write all children
        rootJob.getDownstreamBuilds().forEach(Report::writeJobsInfoToXml);
    }

    public static void writeToReport(BuildInfo rootBuild) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            // use factory to get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            // create instance of DOM
            Document document = db.newDocument();
            // create the root element
            Element root = document.createElement("Builds");
          /*  root.setAttribute("branch", BRANCH_NAME);
            root.setAttribute("log", DATE_NAME);*/
            // append jobs
            appendBuilds(document, root, rootBuild);
            // append root
            document.appendChild(root);
            // add xsl
            Node pi = document.createProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"index.xsl\"");
            document.insertBefore(pi, root);
            // write to file
            try {
                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.setOutputProperty(OutputKeys.METHOD, "xml");
                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                // send DOM to file
                tr.transform(new DOMSource(document),
                        new StreamResult(new FileOutputStream(OUTPUT_XML)));

            } catch (TransformerException | IOException te) {
                System.out.println(te.getMessage());
            }
        } catch (ParserConfigurationException pce) {
            System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
        }
    }

    private static void appendBuilds(Document document, Element builds, BuildInfo buildInfo) {
        // append job
        appendBuild(document, builds, buildInfo);
        // append downstream jobs
        buildInfo.getDownstreamBuilds().stream().filter(BuildInfo::isFound).forEach(info -> appendBuilds(document, builds, info));
    }

    private static void appendBuild(Document document, Element builds, BuildInfo buildInfo) {
        // record element
        Element record = document.createElement("Build");
        Element name = document.createElement("Name");
        name.setTextContent(buildInfo.getBuildName());
        Element jobName = document.createElement("JobName");
        jobName.setTextContent(String.valueOf(buildInfo.getJobName()));
        Element number = document.createElement("Number");
        number.setTextContent(String.valueOf(buildInfo.getBuildNumber()));
        Element parameters = document.createElement("Parameters");
        parameters.setTextContent(String.valueOf(buildInfo.getParameters()));
        Element logSize = document.createElement("LogSize");
        logSize.setTextContent(String.valueOf(buildInfo.getLogSize()));
        Element time = document.createElement("BuildTime");
        time.setTextContent(String.valueOf(buildInfo.getBuildTime()));
        Element stackTrace = document.createElement("StackTrace");
        stackTrace.setTextContent(buildInfo.getResultExceptions());
        Element result = document.createElement("BuildResult");
        result.setTextContent(buildInfo.getResult());
        Element pathToResult = document.createElement("PathToResult");
        pathToResult.setTextContent(buildInfo.getPathToS3());
        // append to record
        record.appendChild(name);
        record.appendChild(jobName);
        record.appendChild(number);
        record.appendChild(parameters);
        record.appendChild(logSize);
        record.appendChild(time);
        record.appendChild(stackTrace);
        record.appendChild(result);
        record.appendChild(pathToResult);
        // append builds
        builds.appendChild(record);
    }

    public static void copyTemplates() {
        File reporterDir = new File(OUTPUT_DIR);
        String reporterDirPath = reporterDir.getAbsolutePath();
        if (!reporterDir.exists()) reporterDir.mkdir();
        File[] templates = new File(TEMPLATES_DIR).listFiles();
        for (File template : templates) {
//                FileUtils.copyFileToDirectory(template, reporterDir);
            copyFile2(template, new File(reporterDirPath + File.separator + template.getName()));
        }
    }

    public static void copyFile(File source, File dest) {
        try (InputStream is = new BufferedInputStream(new FileInputStream(source));
             OutputStream os = new BufferedOutputStream(new FileOutputStream(dest))
        ) {
            int length;
            while ((length = is.read()) > 0) {
                os.write(length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyFile2(File source, File dest) {
        try (InputStream is = new FileInputStream(source);
             OutputStream os = new FileOutputStream(dest)
        ) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        convertToSqLite();
        File source = new File("C:\\Users\\kgromov\\Desktop\\jenkinsAPI\\consoleText_dev_compile.txt");
        File dest1 = new File("C:\\Users\\kgromov\\Desktop\\consoleText_dev_compile1.txt");
        File dest2 = new File("C:\\Users\\kgromov\\Desktop\\consoleText_dev_compile2.txt");
        long start1 = System.nanoTime();
        copyFile(source, dest1);
        System.out.println("Time to copy =" + (System.nanoTime() - start1));
        long start2 = System.nanoTime();
        copyFile(source, dest2);
        System.out.println("Time to copy =" + (System.nanoTime() - start2));
    }

}
