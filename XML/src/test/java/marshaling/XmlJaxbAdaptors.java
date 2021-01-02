package marshaling;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by konstantin on 11.12.2017.
 */
public class XmlJaxbAdaptors {

    public static <T> T xmlToObject(String xmlData, Class<T> clazz) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StreamSource streamSource = new StreamSource(new StringReader(xmlData));
            return unmarshaller.unmarshal(streamSource, clazz).getValue();
        } catch (Exception e) {
            throw new AssertionError(String.format("Unable to unmarshall xml %s to object by class %s", xmlData, clazz), e);
        }
    }

    public static String objectToXml(Object object, Class clazz) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            Marshaller marshaller = jaxbContext.createMarshaller();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            marshaller.marshal(object, baos);
            byte[] content = baos.toByteArray();
            return new String(content);
        } catch (Exception e) {
            throw new AssertionError(String.format("Unable to marshal object by class %s to xml", clazz), e);

        }
    }

    public static File objectToXml(Object object, Class clazz, String pathToFile) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            Marshaller marshaller = jaxbContext.createMarshaller();
            File xml = new File(pathToFile);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            marshaller.marshal(object, baos);
            return xml;
        } catch (Exception e) {
            throw new AssertionError(String.format("Unable to marhal object by class to xml", clazz), e);

        }
    }

/*    public static Document stringToXml(Object object, Class clazz) {
//        String xml = xmlData.replace("\\r", "").replace("\\t", "").replace("\\t", "").replaceAll("\\s{2,}", "");
//        String xml = StringEscapeUtils.unescapeJava(xmlData);
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            Marshaller marshaller = jaxbContext.createMarshaller();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            marshaller.marshal(object, baos);
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//            return docBuilder.parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));
            return docBuilder.parse(new InputSource(baos));
        } catch (Exception e) {
            throw new RuntimeException("Unable to parse xml cause\t" + e.getMessage(), e);
        }*/

        public static void prettyPrint(String xml) {
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(xmlInput, xmlOutput);

            System.out.println("------------------XML------------------");
            System.out.println(xmlOutput.getWriter().toString());
            System.out.print("------------------XML------------------");
        } catch (Exception e) {
            throw new AssertionError("Unable to print xml", e);
        }
    }


}
