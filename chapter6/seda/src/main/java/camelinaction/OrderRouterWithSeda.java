package camelinaction;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * A set of routes that watches a directory for new orders, reads them, converts the order 
 * file into a JMS Message and then sends it to the JMS incomingOrders queue hosted 
 * on an embedded ActiveMQ broker instance.
 * 
 * From there a content-based router is used to send the order to either the
 * xmlOrders or csvOrders queue.
 */
public class OrderRouterWithSeda {

    public static void main(String args[]) throws Exception {
        // create CamelContext
        CamelContext context = new DefaultCamelContext();
        
        // connect to embedded ActiveMQ JMS broker
        ConnectionFactory connectionFactory = 
            new ActiveMQConnectionFactory("vm://localhost");
        context.addComponent("jms",
            JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        // add our route to the CamelContext
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
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
                });
                from("jms:production").process(new Processor() {
                    public void process(Exchange exchange) throws Exception {
                        System.out.println("Production received order: "
                                + exchange.getIn().getHeader("CamelFileName"));  
                    }
                });           
            }
        });

        // start the route and let it do its work
        context.start();
        Thread.sleep(10000);

        // stop the CamelContext
        context.stop();
    }
}
