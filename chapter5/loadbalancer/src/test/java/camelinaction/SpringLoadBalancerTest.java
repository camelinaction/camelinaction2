package camelinaction;

import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * A Spring XML based version of the {@link camelinaction.LoadBalancerTest}.
 * <p/>
 * See the other class for more comments.
 */
public class SpringLoadBalancerTest extends CamelSpringTestSupport {

    @Test
    public void testLoadBalancer() throws Exception {
        MockEndpoint a = getMockEndpoint("mock:a");
        a.expectedBodiesReceived("Hello", "Cool");

        MockEndpoint b = getMockEndpoint("mock:b");
        b.expectedBodiesReceived("Camel rocks", "Bye");

        template.sendBody("direct:start", "Hello");
        template.sendBody("direct:start", "Camel rocks");
        template.sendBody("direct:start", "Cool");
        template.sendBody("direct:start", "Bye");

        assertMockEndpointsSatisfied();
    }

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/loadbalancer.xml");
    }
}
