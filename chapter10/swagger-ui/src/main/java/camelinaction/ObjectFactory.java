package camelinaction;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * To support XML marshalling with camel-jaxb. In this class we map from XML to our POJO classes.
 */
@XmlRegistry
public class ObjectFactory {

    private final QName orderQName = new QName("", "order");

    public ObjectFactory() {
    }

    @XmlElementDecl(namespace = "", name = "order")
    public JAXBElement<Order> createOrder(Order value) {
        return new JAXBElement<Order>(orderQName, Order.class, null, value);
    }

}
