package camelinaction;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Demonstrates how to use a custom thread pool with the WireTap EIP pattern.
 */
public class SpringWireTapTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/wiretap.xml");
    }

    @Test
    public void testWireTap() throws Exception {
        getMockEndpoint("mock:result").expectedBodiesReceived("Hello Camel");
        getMockEndpoint("mock:tap").expectedBodiesReceived("Hello Camel");

        template.sendBody("direct:start", "Hello Camel");

        assertMockEndpointsSatisfied();
    }

}
