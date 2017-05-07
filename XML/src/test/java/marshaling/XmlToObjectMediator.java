package marshaling;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
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
}
