package camelinaction;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Demonstrates how to use the Load Balancer EIP pattern.
 * <p/>
 * Using the random strategy.
 */
public class SpringRandomLoadBalancerTest extends CamelSpringTestSupport {

    @Test
    public void testLoadBalancer() throws Exception {
        // send in 4 messages
        template.sendBody("direct:start", "Hello");
        template.sendBody("direct:start", "Camel rocks");
        template.sendBody("direct:start", "Cool");
        template.sendBody("direct:start", "Bye");
    }

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/random-loadbalancer.xml");
    }
}
