package example;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;

/**
 * Created by konstantin on 11.12.2017.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Foo {

    @XmlElementRef(name="bar")
    private JAXBElement<Bar> bar;

}