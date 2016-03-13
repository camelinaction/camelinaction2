package camelinaction;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Example how to in case of an Exception call the route again from the beginning,
 * instead of at the point of the error.
 * When you run this example you should see the foo route, logging that its being
 * called and output the number of redelivery attempts.
 */
public class SpringRedeliveryBeginRouteTest extends CamelSpringTestSupport {

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("camelinaction/SpringRedeliveryBeginRouteTest.xml");
    }

    @Test
    public void testRedeliverEntireRoute() throws Exception {
        getMockEndpoint("mock:a").expectedMessageCount(1);
        // mock:b will be called 1 and 3 redelivery times
        getMockEndpoint("mock:b").expectedMessageCount(1 + 3);
        // we will fail so there is no message that goes all the way to result
        getMockEndpoint("mock:result").expectedMessageCount(0);

        try {
            template.sendBody("direct:start", "Hello World");
            fail("Should fail");
        } catch (Exception e) {
            // expected
        }

        assertMockEndpointsSatisfied();
    }

}
