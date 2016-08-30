package camelinaction;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Basic test using camel-test for testing a standalone Camel route
 */
public class HelloRouteTest extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        // use the hello route for testing
        return new HelloRoute();
    }

    @Test
    public void testHello() throws Exception {
        // call the url using the fluent template producer (with no body)
        Object out = fluentTemplate.to("jetty:http://localhost:8080/hello").request(String.class);
        // assert that the reply message is what we expect
        assertEquals("Hello from Camel", out);
    }

}
