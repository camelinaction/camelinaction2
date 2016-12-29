package camelinaction;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hystrix using timeout and fallback using XML DSL with Spring
 */
public class SpringCamelHystrixTimeoutAndFallbackTest extends CamelSpringTestSupport {

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("camelinaction/CamelHystrixTimeoutAndFallbackTest.xml");
    }

    @Test
    public void testFast() throws Exception {
        // this calls the fast route and therefore we get a response
        Object out = template.requestBody("direct:start", "fast");
        assertEquals("Fast response", out);
    }

    @Test
    public void testSlow() throws Exception {
        // this calls the slow route and therefore causes a timeout which triggers the fallback
        Object out = template.requestBody("direct:start", "slow");
        assertEquals("Fallback response", out);
    }

}
