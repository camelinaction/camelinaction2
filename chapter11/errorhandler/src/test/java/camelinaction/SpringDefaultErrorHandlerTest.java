package camelinaction;

import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Showing how using default error handler to attempt redelivery
 */
public class SpringDefaultErrorHandlerTest extends CamelSpringTestSupport {

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("camelinaction/SpringDefaultErrorHandlerTest.xml");
    }

    @Test
    public void testOrderOk() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:queue.order");
        mock.expectedBodiesReceived("amount=1,name=Camel in Action,id=123,status=OK");

        template.sendBody("seda:queue.inbox","amount=1,name=Camel in Action");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testOrderFail() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:queue.order");
        mock.expectedMessageCount(0);

        template.sendBody("seda:queue.inbox","amount=1,name=ActiveMQ in Action");

        // wait 5 seconds to let this test run as we expect 0 messages
        Thread.sleep(5000);

        assertMockEndpointsSatisfied();
    }
}
