package camelinaction;

import static org.apache.camel.component.jms.JmsComponent.jmsComponentClientAcknowledge;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class OrderRouterWithSedaTest extends CamelTestSupport {

    @Test
    public void testPrintFile() throws Exception {
        getMockEndpoint("mock:accounting").expectedMessageCount(1);
        getMockEndpoint("mock:production").expectedMessageCount(1);
 
        // should have one incoming message for each queue

        assertMockEndpointsSatisfied();
    }

    protected CamelContext createCamelContext() throws Exception {
        CamelContext camelContext = super.createCamelContext();

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
        camelContext.addComponent("jms", jmsComponentClientAcknowledge(connectionFactory));

        return camelContext;
    }
    
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // load file orders from src/data into the SEDA queue
                from("file:src/data?noop=true").to("seda:incomingOrders");
        
                // content-based router
                from("seda:incomingOrders")
                .choice()
                    .when(header("CamelFileName").endsWith(".xml"))
                        .to("seda:xmlOrders")  
                    .when(header("CamelFileName").endsWith(".csv"))
                        .to("seda:csvOrders");

                from("seda:xmlOrders?multipleConsumers=true").to("jms:accounting");  
                from("seda:xmlOrders?multipleConsumers=true").to("jms:production");  
                
                // test that our route is working
                from("jms:accounting").process(new Processor() {
                    public void process(Exchange exchange) throws Exception {
                        System.out.println("Accounting received order: "
                                + exchange.getIn().getHeader("CamelFileName"));  
                    }
                }).to("mock:accounting");
                from("jms:production").process(new Processor() {
                    public void process(Exchange exchange) throws Exception {
                        System.out.println("Production received order: "
                                + exchange.getIn().getHeader("CamelFileName"));  
                    }
                }).to("mock:production"); 
            }
        };
    }
}
