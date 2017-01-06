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
        JaxbDataFormat jaxb = new JaxbDataFormat();
        jaxb.setContextPath("camelinaction");

        from("direct:inventory")
            .log("Calling inventory service using JMS")
            .hystrix()
                // call the legacy system using JMS
                .to("jms:queue:inventory")
                // the returned data is in XML format so convert that to POJO using JAXB
                .unmarshal(jaxb)
            .onFallback()
                // fallback and read inventory from classpath which is in XML format
                .transform().constant("resource:classpath:fallback-inventory.xml");
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
