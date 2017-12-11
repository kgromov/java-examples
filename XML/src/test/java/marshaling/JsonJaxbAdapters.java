package marshaling;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import org.eclipse.persistence.jaxb.JAXBContextProperties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created by konstantin on 11.12.2017.
 */
public class JsonJaxbAdapters {

    public static <T> T jsonToObject(String jsonData, Class<T> clazz) {
        try {
           /* ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));*/
            return new ObjectMapper().readValue(jsonData, clazz);
        } catch (IOException e) {
            throw new AssertionError(String.format("Unable to unmarshall json %s to object bu class %s", jsonData, clazz), e);
        }
    }

    public static String objectToJSON(Object object) {
        ObjectMapper mapper = new ObjectMapper();
    /*    AnnotationIntrospector introspector
                = new JaxbAnnotationIntrospector();
        mapper.setAnnotationIntrospector(introspector);*/
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new AssertionError(String.format("Unable to marshall object %s to json", object), e);
        }
    }

    // add dependency to parse into JSONObject directly

    // ________________________________ Eclipse MOXy binding ________________________________
    public static <T> String objectToJSONJaxb(Object object, Class<T> clazz) {
        try {
            System.setProperty("javax.xml.bind.context.factory", "org.eclipse.persistence.jaxb.JAXBContextFactory");
            // Create a JaxBContext
            JAXBContext jc = JAXBContext.newInstance(clazz);
            // Create the Marshaller Object using the JaxB Context
            Marshaller marshaller = jc.createMarshaller();
            // Set the Marshaller media type to JSON or XML
            marshaller.setProperty(JAXBContextProperties.MEDIA_TYPE,
                    "application/json"); // generalize with constants, could be both xml or json
            // Set it to true if you need to include the JSON root element in the JSON output
            marshaller.setProperty(JAXBContextProperties.JSON_INCLUDE_ROOT, true);
            // Set it to true if you need the JSON output to formatted
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            // Marshal the employee object to JSON
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            marshaller.marshal(object, baos);
            byte[] content = baos.toByteArray();
            return new String(content);
        } catch (JAXBException e) {
            throw new AssertionError(String.format("Unable to marshal object by class %s to xml", clazz), e);
        } finally {
            System.setProperty("javax.xml.bind.context.factory", "");
        }
    }

    public static <T> T jsonToObjectJaxb(String jsonData, Class<T> clazz) {
        try {
            System.setProperty("javax.xml.bind.context.factory", "org.eclipse.persistence.jaxb.JAXBContextFactory");
            // Create a JaxBContext
            JAXBContext jc = JAXBContext.newInstance(clazz);
            // Create the Marshaller Object using the JaxB Context
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            // Set the Marshaller media type to JSON or XML
            unmarshaller.setProperty(JAXBContextProperties.MEDIA_TYPE,
                    "application/json"); // generalize with constants, could be both xml or json
            // Set it to true if you need to include the JSON root element in the JSON output
            unmarshaller.setProperty(JAXBContextProperties.JSON_INCLUDE_ROOT, true);
            StreamSource streamSource = new StreamSource(new StringReader(jsonData));
            return unmarshaller.unmarshal(streamSource, clazz).getValue();
        } catch (JAXBException e) {
            throw new AssertionError(String.format("Unable to marshal object by class %s to xml", clazz), e);
        } finally {
            System.setProperty("javax.xml.bind.context.factory", "");
        }
    }

    public static void main(String[] args) {
        Template employee = new Template();
        employee.setFirstName("Dan");
        employee.setLastName("Brawn");
        employee.setGender("Male");
        String result = objectToJSON(employee);
        String result2 = objectToJSONJaxb(employee, Template.class);


        System.out.println(result);
        Template clone = jsonToObject(result, Template.class);
        Template clone2 = jsonToObjectJaxb(result2, Template.class);
    }
}
