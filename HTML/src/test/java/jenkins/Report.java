package jenkins;

/**
 * Created by konstantin on 25.03.2018.
 */
public class Report {
    private static final String LOG_FOLDER = "C:\\Windows\\System32\\LogFiles\\PXUI\\";
    //    private static final String LOG_FOLDER = "C:\\Users\\kgromov\\Desktop\\";
    private static final String TEMPLATES_DIR = "./src/test/resources/templates/ui-logs/";
    private static final String OUTPUT_DIR = "./target/ui-logs/";
    private static final String OUTPUT_XML = "./target/ui-logs/index.xml";

    private Report(){}

    public static void writeJobsInfoToXml(JobInfo root)
    {
        // write root info
        // write all children
        root.getDownstreamJobs().forEach(Report::writeJobsInfoToXml);
    }

    /*
     public static void writeToReport(List<Record> records) {
        long start = System.currentTimeMillis();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            // use factory to get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            // create instance of DOM
            Document document = db.newDocument();
            // create the root element
            Element root = document.createElement("Records");
            root.setAttribute("branch", BRANCH_NAME);
            root.setAttribute("log", DATE_NAME);
            records.forEach(item -> {
                // record element
                Element record = document.createElement("Record");
                Element time = document.createElement("Time");
                time.setTextContent(item.getTime());
                Element type = document.createElement("Type");
                type.setTextContent(item.getType());
                Element name = document.createElement("Name");
                name.setTextContent(item.getName());
                Element stackTrace = document.createElement("StackTrace");
                stackTrace.setTextContent(item.getStackTrace());
                Element prevSteps = document.createElement("PreviousSteps");
                prevSteps.setTextContent(item.getPreviousSteps());
                // append to record
                record.appendChild(time);
                record.appendChild(type);
                record.appendChild(name);
                record.appendChild(stackTrace);
                record.appendChild(prevSteps);
                // append records
                root.appendChild(record);
            });
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
        } finally {
            System.out.println(String.format("Time to write xml with %d records = %d ms", records.size(), System.currentTimeMillis() - start));
        }
    }

    public static void copyTemplates() {
        try {
            File reporterDir = new File(OUTPUT_DIR);
            if (!reporterDir.exists()) reporterDir.mkdir();
            File[] templates = new File(TEMPLATES_DIR).listFiles();
            for (File template : templates) {
                FileUtils.copyFileToDirectory(template, reporterDir);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     */
}
