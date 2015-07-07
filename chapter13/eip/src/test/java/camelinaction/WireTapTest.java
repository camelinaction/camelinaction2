package camelinaction;

import java.util.concurrent.ExecutorService;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.ThreadPoolBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Demonstrates how to use a custom thread pool with the WireTap EIP pattern.
 */
public class WireTapTest extends CamelTestSupport {

    @Test
    public void testWireTap() throws Exception {
        getMockEndpoint("mock:result").expectedBodiesReceived("Hello Camel");
        getMockEndpoint("mock:tap").expectedBodiesReceived("Hello Camel");

        template.sendBody("direct:start", "Hello Camel");

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // create a custom thread pool
                ExecutorService lowPool = new ThreadPoolBuilder(context)
                        .poolSize(1).maxPoolSize(5).build("LowPool");

                // which we want the WireTap to use
                from("direct:start")
                    .log("Incoming message ${body}")
                    .wireTap("direct:tap").executorService(lowPool)
                    .to("mock:result");

                from("direct:tap")
                    .log("Tapped message ${body}")
                    .to("mock:tap");
            }
        };
    }
}
