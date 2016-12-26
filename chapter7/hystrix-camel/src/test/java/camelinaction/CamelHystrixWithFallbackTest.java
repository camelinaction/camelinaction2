package camelinaction;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Tests Camel with Hystrix and fallback using Java DSL
 */
public class CamelHystrixWithFallbackTest extends CamelTestSupport {

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        JndiRegistry jndi = super.createRegistry();
        // register the counter service
        jndi.bind("counter", new CounterService());
        return jndi;
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        // use the hystrix with fallback route
        return new HystrixWithFallbackRoute();
    }

    @Test
    public void testCamelHystrixWithFallback() throws Exception {
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
