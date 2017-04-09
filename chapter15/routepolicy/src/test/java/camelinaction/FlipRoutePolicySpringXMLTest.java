package camelinaction;

import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Testing the FlipRoutePolicy using Spring XML
 */
public class FlipRoutePolicySpringXMLTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("camel-fliproute.xml");
    }

    @Test
    public void testFlipRoutePolicyTest() throws Exception {
        MockEndpoint foo = getMockEndpoint("mock:foo");
        foo.expectedMinimumMessageCount(5);

        MockEndpoint bar = getMockEndpoint("mock:bar");
        bar.expectedMinimumMessageCount(5);

        assertMockEndpointsSatisfied();
    }

}
