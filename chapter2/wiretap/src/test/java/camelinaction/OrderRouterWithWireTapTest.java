package camelinaction;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class OrderRouterWithWireTapTest extends CamelTestSupport {

    @Override
    protected CamelContext createCamelContext() throws Exception {
        // create CamelContext
        CamelContext camelContext = super.createCamelContext();
        
        // connect to embedded ActiveMQ JMS broker
        ConnectionFactory connectionFactory = 
            new ActiveMQConnectionFactory("vm://localhost");
        camelContext.addComponent("jms",
            JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
        
        return camelContext;
    }
    
    @Test
    public void testPlacingOrders() throws Exception {
        getMockEndpoint("mock:wiretap").expectedMessageCount(1);
    	getMockEndpoint("mock:xml").expectedMessageCount(1);
        getMockEndpoint("mock:csv").expectedMessageCount(0);
        assertMockEndpointsSatisfied();
    }
    
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // load file orders from src/data into the JMS queue
                from("file:src/data?noop=true").to("jms:incomingOrders");
        
                // content-based router
                from("jms:incomingOrders")
                    .wireTap("jms:orderAudit")
	                .choice()
	                    .when(header("CamelFileName").endsWith(".xml"))
	                        .to("jms:xmlOrders")  
	                    .when(header("CamelFileName").regex("^.*(csv|csl)$"))
	                        .to("jms:csvOrders");     
                
                // test that our route is working
                from("jms:xmlOrders")
	                .log("Received XML order: ${header.CamelFileName}")
	                .to("mock:xml");                
                
                from("jms:csvOrders")
	                .log("Received CSV order: ${header.CamelFileName}")
	                .to("mock:csv");
                
                from("jms:orderAudit")        
	                .log("Wire tap received order: ${header.CamelFileName}")
	                .to("mock:wiretap");   
            }
        };
    }
}
