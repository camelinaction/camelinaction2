package camelinaction;

import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Demonstrates how to use the Load Balancer EIP pattern.
 * <p/>
 * Using the failover in round robin mode.
 */
public class SpringFailoverRoundRobinLoadBalancerTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/failover-roundrobin-loadbalancer.xml");
    }

    @Test
    public void testLoadBalancer() throws Exception {
        // A should get the 1st and 2nd message
        // Cool will fail at B and then failover to A
        MockEndpoint a = getMockEndpoint("mock:a");
        a.expectedBodiesReceived("Hello", "Boom");

        // B should get the 3rd and 4th
        // Kaboom will fail at A and then failover to B
        MockEndpoint b = getMockEndpoint("mock:b");
        b.expectedBodiesReceived("Bye", "Kaboom");

        // send in 4 messages
        template.sendBody("direct:start", "Hello");
        template.sendBody("direct:start", "Boom");
        template.sendBody("direct:start", "Bye");
        template.sendBody("direct:start", "Kaboom");

        assertMockEndpointsSatisfied();
    }
}
