package camelinaction;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Showing how to use Camel error handlers to handle faults (SOAP faults)
 * when using Spring XML to configure the routes
 */
public class SpringHandleFaultTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("camelinaction/HandleFaultTest.xml");
    }

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        JndiRegistry jndi = super.createRegistry();
        jndi.bind("orderService", new OrderService());
        return jndi;
    }

    @Test
    public void testOrderOk() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:queue.order");
        mock.expectedMessageCount(1);

        template.sendBody("seda:queue.inbox","amount=1,name=Camel in Action");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testOrderFail() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:queue.order");
        mock.expectedMessageCount(0);

        MockEndpoint dead = getMockEndpoint("mock:dead");
        dead.expectedMessageCount(1);
        // and on the EXCEPTION_CAUGHT property we have the caused exception which we can assert contains the fault message
        dead.message(0).exchangeProperty(Exchange.EXCEPTION_CAUGHT).convertTo(String.class).contains("ActiveMQ in Action is out of stock");

        template.sendBody("seda:queue.inbox","amount=1,name=ActiveMQ in Action");

        assertMockEndpointsSatisfied();
    }

}
