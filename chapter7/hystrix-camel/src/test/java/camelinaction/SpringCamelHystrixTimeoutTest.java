package camelinaction;

import java.util.concurrent.TimeoutException;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hystrix using timeout using XML DSL with Spring
 */
public class SpringCamelHystrixTimeoutTest extends CamelSpringTestSupport {

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("camelinaction/CamelHystrixTimeoutTest.xml");
    }

    @Test
    public void testFast() throws Exception {
        // this calls the fast route and therefore we get a response
        Object out = template.requestBody("direct:start", "fast");
        assertEquals("Fast response", out);
    }

    @Test
    public void testSlow() throws Exception {
        // this calls the slow route and therefore causes a timeout which triggers an exception
        try {
            template.requestBody("direct:start", "slow");
            fail("Should fail due timeout");
        } catch (Exception e) {
            // expected a timeout
            assertIsInstanceOf(TimeoutException.class, e.getCause().getCause());
        }
    }
}
