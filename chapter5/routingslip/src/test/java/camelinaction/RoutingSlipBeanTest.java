package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * An example how to use Routing Slip EIP.
 * <p/>
 * This example invokes a bean which has been annotated with @RoutingSlip.
 */
public class RoutingSlipBeanTest extends CamelTestSupport {

    @Test
    public void testRoutingSlip() throws Exception {
        // setup expectations that only A and C will receive the message
        getMockEndpoint("mock:a").expectedMessageCount(1);
        getMockEndpoint("mock:b").expectedMessageCount(0);
        getMockEndpoint("mock:c").expectedMessageCount(1);

        // send the incoming message
        template.sendBody("direct:start", "Hello World");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testRoutingSlipCool() throws Exception {
        // setup expectations that all will receive the message
        getMockEndpoint("mock:a").expectedMessageCount(1);
        getMockEndpoint("mock:b").expectedMessageCount(1);
        getMockEndpoint("mock:c").expectedMessageCount(1);

        template.sendBody("direct:start", "We are Cool");

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                    // just a regular bean invocation in Camel
                    // notice this route has no notion of RoutingSlip
                    // its just a regular bean method call
                    .bean(SlipBean.class);
            }
        };
    }
}
