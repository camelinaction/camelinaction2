package camelinaction;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Demonstrates how to use the Load Balancer EIP pattern.
 * <p/>
 * This example sends 2 messages to a Camel route which then sends
 * the message to external services (A and B). We use a failover load balancer
 * in between to send failed messages to the secondary service B in case A failed.
 * <p/>
 * In this example we also let the Camel error handler play a role as it will handle
 * the failure first, and only when it gives up we let the failover load balancer
 * react and attempt failover.
 */
public class FailoverInheritErrorHandlerLoadBalancerTest extends CamelTestSupport {

    @Test
    public void testLoadBalancer() throws Exception {
        // simulate error when sending to service A
        context.getRouteDefinition("start").adviceWith(context, new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                interceptSendToEndpoint("direct:a")
                    .choice()
                        .when(body().contains("Kaboom"))
                            .throwException(new IllegalArgumentException("Damn"))
                        .end()
                    .end();
            }
        });
        context.start();

        // A should get the 1st
        MockEndpoint a = getMockEndpoint("mock:a");
        a.expectedBodiesReceived("Hello");

        // B should get the 2nd
        MockEndpoint b = getMockEndpoint("mock:b");
        b.expectedBodiesReceived("Kaboom");

        // send in 2 messages
        template.sendBody("direct:start", "Hello");
        template.sendBody("direct:start", "Kaboom");

        assertMockEndpointsSatisfied();
    }

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // configure error handler to try at most 3 times with 2 sec delay
                errorHandler(defaultErrorHandler()
                    .maximumRedeliveries(3).redeliveryDelay(2000)
                    // reduce some logging noise
                    .retryAttemptedLogLevel(LoggingLevel.WARN)
                    .retriesExhaustedLogLevel(LoggingLevel.WARN)
                    .logStackTrace(false));

                from("direct:start").routeId("start")
                    // use load balancer with failover strategy
                    // 1 = which will try 1 failover attempt before exhausting
                    // true = do use Camel error handling
                    // false = do not use round robin mode
                    .loadBalance().failover(1, true, false)
                        // will send to A first, and if fails then send to B afterwards
                        .to("direct:a").to("direct:b")
                    .end();

                // service A
                from("direct:a")
                    .log("A received: ${body}")
                    .to("mock:a");

                // service B
                from("direct:b")
                    .log("B received: ${body}")
                    .to("mock:b");
            }
        };
    }

}
