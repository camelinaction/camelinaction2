package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Example how to in case of an Exception call the route again from the beginning,
 * instead of at the point of the error.
 * When you run this example you should see the foo route, logging that its being
 * called and output the number of redelivery attempts.
 */
public class RedeliveryBeginRouteTest extends CamelTestSupport {

    @Test
    public void testRedeliverEntireRoute() throws Exception {
        getMockEndpoint("mock:a").expectedMessageCount(1);
        // mock:b will be called 1 and 3 redelivery times
        getMockEndpoint("mock:b").expectedMessageCount(1 + 3);
        // we will fail so there is no message that goes all the way to result
        getMockEndpoint("mock:result").expectedMessageCount(0);

        try {
            template.sendBody("direct:start", "Hello World");
            fail("Should fail");
        } catch (Exception e) {
            // expected
        }

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // redelivery settings
                onException(IllegalArgumentException.class)
                        .maximumRedeliveries(3);

                from("direct:start")
                        .to("mock:a")
                        // this route has error handler, so any exception will redeliver (eg calling the foo route again)
                        .to("direct:foo")
                        .to("mock:result");

                // this route has no error handler, so any exception will not be redelivered
                from("direct:foo")
                        .errorHandler(noErrorHandler())
                        .log("Calling foo route redelivery count: ${header.CamelRedeliveryCounter}")
                        .to("mock:b")
                        .throwException(new IllegalArgumentException("Forced"));
            }
        };
    }
}
