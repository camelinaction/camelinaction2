package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Demonstrates how to use the Load Balancer EIP pattern.
 * <p/>
 * This example sends 4 messages to a Camel route which then sends
 * the message to external services (A and B). We use a failover load balancer
 * in between to send failed messages to the secondary service B in case A failed.
 */
public class FailoverLoadBalancerTest extends CamelTestSupport {

    @Test
    public void testLoadBalancer() throws Exception {
        // A should get the 1st, 3rd and 4th message
        MockEndpoint a = getMockEndpoint("mock:a");
        a.expectedBodiesReceived("Hello", "Cool", "Bye");

        // B should get the 2nd
        MockEndpoint b = getMockEndpoint("mock:b");
        b.expectedBodiesReceived("Kaboom");

        // send in 4 messages
        template.sendBody("direct:start", "Hello");
        template.sendBody("direct:start", "Kaboom");
        template.sendBody("direct:start", "Cool");
        template.sendBody("direct:start", "Bye");

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                    // use load balancer with failover strategy
                    .loadBalance().failover()
                        // will send to A first, and if fails then send to B afterwards
                        .to("direct:a").to("direct:b")
                    .end();

                // service A
                from("direct:a")
                    .log("A received: ${body}")
                    // in case of Kaboom the throw an exception to simulate failure
                    .choice()
                        .when(body().contains("Kaboom"))
                            .throwException(new IllegalArgumentException("Damn"))
                        .end()
                    .end()
                    .to("mock:a");

                // service B
                from("direct:b")
                    .log("B received: ${body}")
                    .to("mock:b");
            }
        };
    }

}
