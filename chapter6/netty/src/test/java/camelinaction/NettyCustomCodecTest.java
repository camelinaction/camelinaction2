package camelinaction;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import static org.apache.camel.component.jms.JmsComponent.jmsComponentClientAcknowledge;

public class NettyCustomCodecTest extends CamelTestSupport {

    private static final String STATUS_GOOD = "MachineID=2371748;Status=Good";

    @Override    
    protected CamelContext createCamelContext() throws Exception {
        CamelContext camelContext = super.createCamelContext();

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
        camelContext.addComponent("jms", jmsComponentClientAcknowledge(connectionFactory));

        return camelContext;
    }
    
    protected JndiRegistry createRegistry() throws Exception {
        JndiRegistry jndi = super.createRegistry();
        jndi.bind("welderDecoder", new WelderDecoder());        
        jndi.bind("welderEncoder", new WelderEncoder());        
        return jndi;
    }
    
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            
            @Override
            public void configure() throws Exception {
                from("netty4:tcp://localhost:8998?encoder=#welderEncoder&decoder=#welderDecoder&sync=false").to("jms:operations");
                
                from("jms:operations").to("mock:end");
            }
        };
    }

    @Test
    public void testSendToTcp() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:end");
        mock.expectedBodiesReceived(STATUS_GOOD);

        template.sendBody("netty4:tcp://localhost:8998?encoder=#welderEncoder&decoder=#welderDecoder&sync=false", "23717481");

        mock.assertIsSatisfied();
    }

}
