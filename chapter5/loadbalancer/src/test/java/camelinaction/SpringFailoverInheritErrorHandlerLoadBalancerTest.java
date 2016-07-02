package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Demonstrates how to use the Load Balancer EIP pattern.
 */
public class SpringFailoverInheritErrorHandlerLoadBalancerTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/failover-inheriterrorhandler-loadbalancer.xml");
    }

    @Test
    public void testLoadBalancer() throws Exception {
        // simulate error when sending to service A
        context.getRouteDefinition("start").adviceWith(context, new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                interceptSendToEndpoint("direct:a")
                    .choice()
                        .when(body().contains("Kaboom"))
                            .throwException(new IllegalArgumentException("Damn"))
                        .end()
                    .end();
            }
        });
        context.start();

        // A should get the 1st
        MockEndpoint a = getMockEndpoint("mock:a");
        a.expectedBodiesReceived("Hello");

        // B should get the 2nd
        MockEndpoint b = getMockEndpoint("mock:b");
        b.expectedBodiesReceived("Kaboom");

        // send in 2 messages
        template.sendBody("direct:start", "Hello");
        template.sendBody("direct:start", "Kaboom");

        assertMockEndpointsSatisfied();
    }

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }
}
