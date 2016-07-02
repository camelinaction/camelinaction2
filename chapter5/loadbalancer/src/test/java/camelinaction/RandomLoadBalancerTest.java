package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Demonstrates how to use the Load Balancer EIP pattern.
 * <p/>
 * Using the random strategy.
 */
public class RandomLoadBalancerTest extends CamelTestSupport {

    @Test
    public void testLoadBalancer() throws Exception {
        // send in 4 messages
        template.sendBody("direct:start", "Hello");
        template.sendBody("direct:start", "Camel rocks");
        template.sendBody("direct:start", "Cool");
        template.sendBody("direct:start", "Bye");
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                    // use load balancer with random strategy
                    .loadBalance().random()
                        // this is the 3 processors which we will balance across
                        .to("seda:a").to("seda:b").to("seda:c")
                    .end();

                // service A
                from("seda:a").log("A received: ${body}");

                // service B
                from("seda:b").log("B received: ${body}");

                // service C
                from("seda:c").log("C received: ${body}");
            }
        };
    }

}
