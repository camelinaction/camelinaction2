package camelinaction;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * A basic example how to use Routing Slip EIP.
 * <p/>
 * This example is a simple example how to route a message using the routing slip EIP
 * based on an existing header which dictates the sequence steps.
 */
public class SpringRoutingSlipSimpleTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/routingslip-simple.xml");
    }

    @Test
    public void testRoutingSlip() throws Exception {
        // setup expectations that only A and C will receive the message
        getMockEndpoint("mock:a").expectedMessageCount(1);
        getMockEndpoint("mock:b").expectedMessageCount(0);
        getMockEndpoint("mock:c").expectedMessageCount(1);

        // send the incoming message with the attached slip
        template.sendBodyAndHeader("direct:start", "Hello World", "mySlip", "mock:a,mock:c");

        assertMockEndpointsSatisfied();
    }

}
