package camelinaction;

import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Demonstrates how to use the Load Balancer EIP pattern.
 * <p/>
 * Using the topic strategy.
 */
public class SpringTopicLoadBalancerTest extends CamelSpringTestSupport {

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
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/topic-loadbalancer.xml");
    }
}
