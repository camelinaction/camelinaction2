package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Demonstrates how to use the Load Balancer EIP pattern.
 * <p/>
 * Using the topic strategy.
 */
public class TopicLoadBalancerTest extends CamelTestSupport {

    @Test
    public void testLoadBalancer() throws Exception {
        // both mocks should get all the messages as its topic strategy
        MockEndpoint a = getMockEndpoint("mock:a");
        a.expectedMessageCount(4);

        MockEndpoint b = getMockEndpoint("mock:b");
        b.expectedMessageCount(4);

        // send in 4 messages
        template.sendBody("direct:start", "Hello");
        template.sendBody("direct:start", "Camel rocks");
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
                    // use load balancer with topic strategy
                    .loadBalance().topic()
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
