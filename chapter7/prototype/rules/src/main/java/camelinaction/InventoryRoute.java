package camelinaction;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.converter.jaxb.JaxbDataFormat;

/**
 * Camel route that calls the legacy system
 */
@ApplicationScoped
public class InventoryRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // use JAXB to convert XML to POJO classes
        JaxbDataFormat jaxb = new JaxbDataFormat("camelinaction");

        from("direct:start")
            // call the legacy system using JMS
            .to("jms:queue:inventory")
            // the returned data is in XML format so convert that to POJO using JAXB
            .unmarshal(jaxb);
    }

    /**
     * Setup JMS component
     */
    @Produces
    @Named("jms")
    public static JmsComponent jmsComponent() {
        ActiveMQComponent jms = new ActiveMQComponent();
        jms.setBrokerURL("tcp://localhost:61616");
        return jms;
    }
}
