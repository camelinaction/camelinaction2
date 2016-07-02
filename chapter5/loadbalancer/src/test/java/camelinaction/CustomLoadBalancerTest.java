package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Demonstrates how to use the Load Balancer EIP pattern.
 * <p/>
 * In this example we use a custom load balancer {@link camelinaction.MyCustomLoadBalancer} which dictates
 * how messages is being balanced at runtime.
 */
public class CustomLoadBalancerTest extends CamelTestSupport {

    @Test
    public void testLoadBalancer() throws Exception {
        // A should get the gold messages
        MockEndpoint a = getMockEndpoint("mock:a");
        a.expectedBodiesReceived("Camel rocks", "Cool");

        // B should get the other messages
        MockEndpoint b = getMockEndpoint("mock:b");
        b.expectedBodiesReceived("Hello", "Bye");

        // send in 4 messages
        template.sendBodyAndHeader("direct:start", "Hello", "type", "silver");
        template.sendBodyAndHeader("direct:start", "Camel rocks", "type", "gold");
        template.sendBodyAndHeader("direct:start", "Cool", "type", "gold");
        template.sendBodyAndHeader("direct:start", "Bye", "type", "bronze");

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                    // use a custom load balancer
                    .loadBalance(new MyCustomLoadBalancer())
                        // this is the 2 processors which we will balance across
                        .to("seda:a").to("seda:b")
                    .end();

                // service A
                from("seda:a")
                    .log("A received: ${body}")
                    .to("mock:a");

                // service B
                from("seda:b")
                    .log("B received: ${body}")
                    .to("mock:b");
            }
        };
    }

}
