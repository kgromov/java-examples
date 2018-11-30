package here;

import com.google.common.collect.Sets;
import javafx.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DependencyResolver {
    private static final Pattern PLACE_HOLDER = Pattern.compile("\\$\\{\\w+}");

    private static final FileFilter XML_WF_FILTER = file ->
    {
        String fileName = file.getName();
        return fileName.contains(".xml") && fileName.contains("Workflow");
    };

    public static Map<String, String> parseDependenciesToVersion(String pathToPom) {
        Map<String, String> artifactIdToVersion = new HashMap<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(pathToPom));
            XPathFactory xpathfactory = XPathFactory.newInstance();
            XPath xpath = xpathfactory.newXPath();
            XPathExpression expr = xpath.compile("//dependencies/dependency/version");
            NodeList versionedDependencies = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

            for (int i = 0; i < versionedDependencies.getLength(); i++) {
                Node versionNode = versionedDependencies.item(i);
                String version = versionNode.getTextContent();
                String artifactId = ((Element) versionNode.getParentNode()).getElementsByTagName("artifactId").item(0).getTextContent();
                artifactIdToVersion.putIfAbsent(artifactId, version);
            }
            return artifactIdToVersion;
        } catch (ParserConfigurationException | IOException | SAXException | XPathExpressionException e) {
            return new HashMap<>();
        }
    }

    public static Map<String, Pair<String, String>> getDifferenceInVersions(Map<String, String> map1, Map<String, String> map2) {
        Map<String, Pair<String, String>> diffMap = new HashMap<>();
        Sets.intersection(map1.keySet(), map2.keySet()).forEach(artifactId ->
        {
            String version1 = map1.get(artifactId);
            String version2 = map2.get(artifactId);
            if (!version1.equals(version2)) {
                diffMap.put(artifactId, new Pair<>(version1, version2));
            }
        });
        return diffMap;
    }

    public static void printAllParametersByWorkFlow(File root, Set<String> parameters) {
        if (root.isDirectory()) {
            File[] files = root.listFiles();
            for (File file : files) {
                printAllParametersByWorkFlow(file, parameters);
            }
        } else if (XML_WF_FILTER.accept(root)) {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(root);
                XPathFactory xpathfactory = XPathFactory.newInstance();
                XPath xpath = xpathfactory.newXPath();
                XPathExpression expr = xpath.compile("//Property");
                NodeList properties = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

                System.out.println("=============================" + root.getName() + "====================================");
                for (int i = 0; i < properties.getLength(); i++) {
                    Element property = (Element) properties.item(i);
                    String name = property.getAttribute("Name");
                    String value = property.getAttribute("Value");
                    System.out.println(name + " => " + value);
                    // try to identify jenkins parameters
                    Matcher matcher = PLACE_HOLDER.matcher(value);
                    while (matcher.find()) {
                        parameters.add(matcher.group());
                    }
                    if (name.equals("HDLaneUrl")) {
                        int a = 1;
                    }
                    if (value.isEmpty()) {
                        parameters.add(name);
                    }
                }
            } catch (ParserConfigurationException | IOException | SAXException | XPathExpressionException e) {

            }
        }
    }

    public static void main(String[] args) {

        Set<String> parameters = new TreeSet<>();
        printAllParametersByWorkFlow(new File("C:\\HERE\\nds_here\\nds_compiler\\basicnav\\etc\\xml"), parameters);
        int a = 1;
      /*  Map<String, String> laneDependencies = parseDependenciesToVersion("C:\\HERE\\nds_here\\lane_module\\pom.xml");
        Map<String, String> compilierDependencies = parseDependenciesToVersion("C:\\HERE\\nds_here\\nds_compiler\\pom.xml");
        Map<String, Pair<String, String>> diff = getDifferenceInVersions(laneDependencies, compilierDependencies);
        Set<String> notIncludedDependencies = Sets.difference(laneDependencies.keySet(), compilierDependencies.keySet());
        int a = 1;*/
    }
}
