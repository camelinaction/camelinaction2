package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.spi.RoutePolicy;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Testing the FlipRoutePolicy using Java DSL
 */
public class FlipRoutePolicyJavaDSLTest extends CamelTestSupport {

    @Test
    public void testFlipRoutePolicyTest() throws Exception {
        MockEndpoint foo = getMockEndpoint("mock:foo");
        foo.expectedMinimumMessageCount(5);

        MockEndpoint bar = getMockEndpoint("mock:bar");
        bar.expectedMinimumMessageCount(5);

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // create the flip route policy
                RoutePolicy policy = new FlipRoutePolicy("foo", "bar");

                // use the flip route policy in the foo route
                from("timer://foo?delay=500")
                    .routeId("foo").routePolicy(policy)
                    .setBody().constant("Foo message")
                    .to("log:foo")
                    .to("mock:foo");

                // use the flip route policy in the bar route and do NOT start
                // this route on startup
                from("timer://bar?delay=500")
                    .routeId("bar").routePolicy(policy).noAutoStartup()
                    .setBody().constant("Bar message")
                    .to("log:bar")
                    .to("mock:bar");
            }
        };
    }
}
