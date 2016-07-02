package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * A basic example how to use Routing Slip EIP.
 * <p/>
 * This example is a simple example how to route a message using the routing slip EIP
 * based on an existing header which dictates the sequence steps.
 */
public class RoutingSlipSimpleTest extends CamelTestSupport {

    @Test
    public void testRoutingSlip() throws Exception {
        // setup expectations that only A and C will receive the message
        getMockEndpoint("mock:a").expectedMessageCount(1);
        getMockEndpoint("mock:b").expectedMessageCount(0);
        getMockEndpoint("mock:c").expectedMessageCount(1);

        // send the incoming message with the attached slip
        template.sendBodyAndHeader("direct:start", "Hello World", "mySlip", "mock:a,mock:c");

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                    // use routing slip with the attached slip
                    // as the header with key: mySlip
                    .routingSlip(header("mySlip"));
            }
        };
    }
}
