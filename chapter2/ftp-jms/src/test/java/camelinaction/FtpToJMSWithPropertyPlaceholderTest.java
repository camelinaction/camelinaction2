package camelinaction;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class FtpToJMSWithPropertyPlaceholderTest extends CamelTestSupport {
	
    @Override
    protected CamelContext createCamelContext() throws Exception {
        // create CamelContext
        CamelContext camelContext = super.createCamelContext();
        
        // connect to embedded ActiveMQ JMS broker
        ConnectionFactory connectionFactory = 
            new ActiveMQConnectionFactory("vm://localhost");
        camelContext.addComponent("jms",
            JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        // setup the properties component to use the test file
        PropertiesComponent prop = camelContext.getComponent("properties", PropertiesComponent.class);
        prop.setLocation("classpath:rider-test.properties");        
        
        return camelContext;
    }
    
    @Test
    public void testPlacingOrders() throws Exception {
        getMockEndpoint("mock:incomingOrders").expectedMessageCount(1);
        assertMockEndpointsSatisfied();
    }
    
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // load file orders from src/data into the JMS queue
                from("file:src/data?noop=true")
                    .to("jms:{{myDest}}");

                // test that our route is working
                from("jms:incomingOrders")
                    .to("mock:incomingOrders");
            }
        };
    }
    
}
