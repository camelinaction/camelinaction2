package camelinaction;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringNewExceptionTest extends CamelSpringTestSupport {

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("camelinaction/SpringNewExceptionTest.xml");
    }

    @Test
    public void testNoError() throws Exception {
        getMockEndpoint("mock:done").expectedMessageCount(1);

        template.sendBodyAndHeader("direct:start", "Hello Camel", "name", "Camel");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testNewException() throws Exception {
        getMockEndpoint("mock:done").expectedMessageCount(0);

        try {
            template.sendBodyAndHeader("direct:start", "Hello Bomb", "name", "Kaboom");
            fail("Should have thrown exception");
        } catch (CamelExecutionException e) {
            // we expect a NullPointerException because that is what NotAllowedProcessor throws
            // while handling the first AuthorizationException which is thrown from the filter in the route
            assertIsInstanceOf(NullPointerException.class, e.getCause());
        }

        assertMockEndpointsSatisfied();
    }
}
