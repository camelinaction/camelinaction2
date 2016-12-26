package camelinaction;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Tests Camel with Hystrix using XML DSL with Spring
 */
public class SpringCamelHystrixWithFallbackTest extends CamelSpringTestSupport {

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("camelinaction/CamelHystrixWithFallbackSpringTest.xml");
    }

    @Test
    public void testCamelHystrix() throws Exception {
        Object out1 = template.requestBody("direct:start", "Hello");
        assertEquals("Count 1", out1);

        Object out2 = template.requestBody("direct:start", "Hello");
        assertEquals("Count 2", out2);

        Object out3 = template.requestBody("direct:start", "Hello");
        assertEquals("Count 3", out3);

        Object out4 = template.requestBody("direct:start", "Hello");
        assertEquals("Count 4", out4);

        // should not fail the 5th time
        Object out5 = template.requestBody("direct:start", "Hello");
        assertEquals("No Counter", out5);
    }
}
