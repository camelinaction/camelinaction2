package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Dynamic Router EIP example
 */
public class DynamicRouterTest extends CamelTestSupport {

    @Test
    public void testDynamicRouter() throws Exception {
        getMockEndpoint("mock:a").expectedBodiesReceived("Camel");
        getMockEndpoint("mock:result").expectedBodiesReceived("Bye Camel");

        template.sendBody("direct:start", "Camel");

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                    // use Dynamic Router EIP to route message dynamically
                    // use the DynamicRouterBean bean which provides the logic
                    // to compute where the message should go
                    .dynamicRouter(method(DynamicRouterBean.class, "route"))
                    // when we are done with the Dynamic Router EIP go to
                    // the mock result endpoint
                    .to("mock:result");
            }
        };
    }
}
