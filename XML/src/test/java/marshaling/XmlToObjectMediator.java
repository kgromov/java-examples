package marshaling;

import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.eclipse.persistence.jaxb.MarshallerProperties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kgr on 11/4/2015.
 */

/**
 * @link https://www.javacodegeeks.com/2014/12/jaxb-tutorial-xml-binding.html
 */
public class XmlToObjectMediator {
    public static final String xmlContactData = "./src/test/resources/data/leads_data_1000.xml";

    private static <T> Object unMarshalToObject(String xml_file_name, Class<T> clazz) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (T) jaxbUnmarshaller.unmarshal(new File(xml_file_name));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void unMarshalToObject(String xml_file_name, Object o) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(o.getClass());
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.marshal(o, new File(xml_file_name));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public static List<Template> getContactList(int desiredSize) {
        List<Template> contactList = new ArrayList<>(desiredSize);
        for (int i = 0; i < desiredSize; i++) {
            contactList.add((Template) unMarshalToObject(xmlContactData, Template.class));
        }
        return contactList;
    }

    public static void main(String[] args) throws JAXBException {
        System.setProperty("javax.xml.bind.context.factory", "org.eclipse.persistence.jaxb.JAXBContextFactory");
        Template employee = new Template();
        employee.setFirstName("Dan");
        employee.setLastName("Brawn");
        // Create a JaxBContext
        JAXBContext jc = JAXBContext.newInstance(Template.class);

        // Create the Marshaller Object using the JaxB Context
        Marshaller marshaller = jc.createMarshaller();

        // Set the Marshaller media type to JSON or XML
        marshaller.setProperty(JAXBContextProperties.MEDIA_TYPE,
                "application/json");

        // Set it to true if you need to include the JSON root element in the JSON output
        marshaller.setProperty(JAXBContextProperties.JSON_INCLUDE_ROOT, true);

        // Set it to true if you need the JSON output to formatted
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        // Marshal the employee object to JSON and print the output to console
        marshaller.marshal(employee, System.out);


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        marshaller.marshal(employee, baos);

        byte[] content = baos.toByteArray();
        System.out.println(new String(content));

        ByteArrayInputStream bais = new ByteArrayInputStream(content);
        Template copy = (Template) jc.createUnmarshaller().unmarshal(bais);


    }
}
