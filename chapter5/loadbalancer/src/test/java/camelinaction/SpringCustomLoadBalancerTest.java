package camelinaction;

import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Demonstrates how to use the Load Balancer EIP pattern.
 * <p/>
 * In this example we use a custom load balancer {@link camelinaction.MyCustomLoadBalancer} which dictates
 * how messages is being balanced at runtime.
 */
public class SpringCustomLoadBalancerTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/custom-loadbalancer.xml");
    }

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
}
