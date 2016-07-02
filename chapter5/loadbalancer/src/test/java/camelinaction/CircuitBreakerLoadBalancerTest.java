package camelinaction;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
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
public class CircuitBreakerLoadBalancerTest extends CamelTestSupport {

    @Test
    public void testLoadBalancer() throws Exception {
        // A should get the 1st, 3rd and 4th message
        MockEndpoint a = getMockEndpoint("mock:a");
        a.expectedBodiesReceived("Got through!");

        // send in 4 messages
        sendMessage("direct:start", "Kaboom");
        sendMessage("direct:start", "Kaboom");
        // circuit should break here as we've had 2 exception occur when accessing remote service
        
        // this call should fail as blocked by circuit breaker
        sendMessage("direct:start", "Blocked");

        // wait so circuit breaker will timeout and go into half-open state
        Thread.sleep(5000);

        // should success
        sendMessage("direct:start", "Got through!");

        assertMockEndpointsSatisfied();
    }

    protected Exchange sendMessage(final String endpoint, final Object body) throws Exception {
        return template.send(endpoint, new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setBody(body);
            }
        });
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                    .loadBalance()
                        .circuitBreaker(2, 2000L, Exception.class)
                        .to("direct:a");

                from("direct:a")
                    .log("A received: ${body}")
                    // in case of Kaboom the throw an exception to simulate failure
                    .choice()
                        .when(body().contains("Kaboom"))
                            .throwException(new IllegalArgumentException("Damn"))
                        .end()
                    .end()
                    .to("mock:a");
            }
        };
    }

}
