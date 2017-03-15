package camelinaction;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringOrderRouterWithMulticastSOETest extends CamelSpringTestSupport {

    @Override
    public void setUp() throws Exception {
        deleteDirectory("activemq-data");
        super.setUp();
    }
    
    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("camelinaction/SpringOrderRouterWithMulticastSOETest.xml");
    }

    @Test
    public void testPlacingOrders() throws Exception {
        getMockEndpoint("mock:accounting_before_exception").expectedMessageCount(1);
        getMockEndpoint("mock:accounting").expectedMessageCount(0);
        getMockEndpoint("mock:production").expectedMessageCount(0);
        assertMockEndpointsSatisfied();
    }
}
