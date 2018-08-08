package here;

import com.google.common.collect.Sets;
import javafx.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DependencyResolver
{

    public static Map<String, String> parseDependenciesToVersion(String pathToPom)
    {
        Map<String, String> artifactIdToVersion = new HashMap<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(pathToPom));
            XPathFactory xpathfactory = XPathFactory.newInstance();
            XPath xpath = xpathfactory.newXPath();
            XPathExpression expr = xpath.compile("//dependencies/dependency/version");
            NodeList versionedDependencies = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

            for (int i = 0; i < versionedDependencies.getLength(); i++)
            {
                Node versionNode = versionedDependencies.item(i);
                String version = versionNode.getTextContent();
                String artifactId = ((Element)versionNode.getParentNode()).getElementsByTagName("artifactId").item(0).getTextContent();
                artifactIdToVersion.putIfAbsent(artifactId, version);
            }
            return artifactIdToVersion;
        } catch (ParserConfigurationException | IOException | SAXException | XPathExpressionException e) {
            return new HashMap<>();
        }
    }

    public static Map<String, Pair<String, String>> getDifferenceInVersions(Map<String, String> map1, Map<String, String> map2)
    {
        Map<String, Pair<String, String>> diffMap = new HashMap<>();
        Sets.intersection(map1.keySet(), map2.keySet()).forEach(artifactId ->
        {
            String version1 = map1.get(artifactId);
            String version2 = map2.get(artifactId);
            if(!version1.equals(version2))
            {
                diffMap.put(artifactId, new Pair<>(version1, version2));
            }
        });
        return diffMap;
    }

    public static void main(String[] args) {
        Map<String, String> laneDependencies = parseDependenciesToVersion("C:\\HERE\\nds_here\\lane_module\\pom.xml");
        Map<String, String> compilierDependencies = parseDependenciesToVersion("C:\\HERE\\nds_here\\nds_compiler\\pom.xml");
        Map<String, Pair<String, String>> diff = getDifferenceInVersions(laneDependencies, compilierDependencies);
        Set<String> notIncludedDependencies = Sets.difference(laneDependencies.keySet(), compilierDependencies.keySet());
        int a =1;
    }
}
