package camelinaction;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Demonstrates how to use the Load Balancer EIP pattern.
 */
public class SpringCircuitBreakerLoadBalancerTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/circuit-breaker-loadbalancer.xml");
    }

    @Test
    public void testLoadBalancer() throws Exception {
        // A should get the 1st, 3rd and 4th message
        MockEndpoint a = getMockEndpoint("mock:a");
        a.expectedBodiesReceived("Got through!");

        // send in 4 messages
        sendMessage("direct:start", "Kaboom");
        sendMessage("direct:start", "Kaboom");
        // circuit should break here as we've had 2 exception occur when accessing remote service
        
        // this call should fail as blocked by circuit breaker
        sendMessage("direct:start", "Blocked");

        // wait so circuit breaker will timeout and go into half-open state
        Thread.sleep(5000);

        // should success
        sendMessage("direct:start", "Got through!");

        assertMockEndpointsSatisfied();
    }

    protected Exchange sendMessage(final String endpoint, final Object body) throws Exception {
        return template.send(endpoint, new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setBody(body);
            }
        });
    }
}
