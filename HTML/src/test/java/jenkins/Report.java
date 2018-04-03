package jenkins;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

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

/**
 * Created by konstantin on 25.03.2018.
 */
public class Report {
    private static final String TEMPLATES_DIR = "C:\\Projects\\java-examples\\HTML\\src\\main\\resources\\templates";
    private static final String OUTPUT_DIR = "C:\\Projects\\java-examples\\HTML\\target\\jenkins-builds";
    private static final String OUTPUT_XML = OUTPUT_DIR + "\\index.xml";

    private Report() {
    }

    // first: create root and than append all build to root -> <Builds>
    public static void writeJobsInfoToXml(JobInfo rootJob) {
        // write root info
        writeToReport(rootJob);
        // write all children
        rootJob.getDownstreamJobs().forEach(Report::writeJobsInfoToXml);
    }

    public static void writeToReport(JobInfo rootBuild) {
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

    private static void appendBuilds(Document document, Element builds, JobInfo buildInfo) {
        // append job
        appendBuild(document, builds, buildInfo);
        // append downstream jobs
        buildInfo.getDownstreamJobs().forEach(info -> appendBuilds(document, builds, info));
    }

    private static void appendBuild(Document document, Element builds, JobInfo buildInfo) {
        // record element
        Element record = document.createElement("Build");
        Element name = document.createElement("Name");
        name.setTextContent(buildInfo.getBuildName());
        Element number = document.createElement("Number");
        number.setTextContent(String.valueOf(buildInfo.getBuildNumber()));
        Element parameters = document.createElement("Parameters");
        parameters.setTextContent(String.valueOf(buildInfo.getParameters()));
        Element logSize = document.createElement("LogSize");
        logSize.setTextContent(String.valueOf(buildInfo.getLogSize()));
        Element time = document.createElement("BuildTime");
        time.setTextContent(String.valueOf(buildInfo.getBuildTime()));
        Element stackTrace = document.createElement("StackTrace");
        stackTrace.setTextContent(buildInfo.getExceptions());
        Element result = document.createElement("BuildResult");
        result.setTextContent(buildInfo.getResult());
        Element pathToResult = document.createElement("PathToResult");
        pathToResult.setTextContent(buildInfo.getPathToS3());
        // append to record
        record.appendChild(name);
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
