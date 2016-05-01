package camelinaction;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringOrderRouterWithRecipientListAnnotationTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("camelinaction/SpringOrderRouterWithRecipientListAnnotationTest.xml");
    }

    @Test
    public void testPlacingOrders() throws Exception {
        getMockEndpoint("mock:accounting").expectedMessageCount(2);
        getMockEndpoint("mock:production").expectedMessageCount(1);
        assertMockEndpointsSatisfied();
    }
}
