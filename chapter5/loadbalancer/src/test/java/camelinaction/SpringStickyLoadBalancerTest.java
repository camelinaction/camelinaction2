package camelinaction;

import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Demonstrates how to use the Load Balancer EIP pattern.
 * <p/>
 * Using the sticky strategy.
 */
public class SpringStickyLoadBalancerTest extends CamelSpringTestSupport {

    @Test
    public void testLoadBalancer() throws Exception {
        // A should get the 1st and 4th message
        MockEndpoint a = getMockEndpoint("mock:a");
        a.expectedBodiesReceived("Hello", "Bye");

        // B should get the 2nd and 3rd message
        MockEndpoint b = getMockEndpoint("mock:b");
        b.expectedBodiesReceived("Camel rocks", "Cool");

        // send in 4 messages with id as correlation key
        // notice that the ids is not an exact number to pick the processor in order.
        // Camel will use the key to generate a hash value which is used for choosing the processor.
        // gold will pick A because its the first message
        // then silver will be bound to pick B as its the next
        template.sendBodyAndHeader("direct:start", "Hello", "type", "gold");
        template.sendBodyAndHeader("direct:start", "Camel rocks", "type", "silver");
        template.sendBodyAndHeader("direct:start", "Cool", "type", "silver");
        template.sendBodyAndHeader("direct:start", "Bye", "type", "gold");

        assertMockEndpointsSatisfied();
    }

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/sticky-loadbalancer.xml");
    }
}
