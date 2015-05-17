package camelinaction;

import org.apache.camel.Exchange;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Showing how using per route scope error handling when using Spring XML to configure the routes
 */
public class SpringRouteScopeTest extends CamelSpringTestSupport {

    @Override
    public void setUp() throws Exception {
        deleteDirectory("target/orders");
        super.setUp();
    }

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        // see this file for the route in Spring XML
        return new ClassPathXmlApplicationContext("camelinaction/RouteScopeTest.xml");
    }

    @Test
    public void testOrderOk() throws Exception {
        // we expect the file to be converted to csv and routed to the 2nd route
        MockEndpoint file = getMockEndpoint("mock:file");
        file.expectedMessageCount(1);

        // we expect the 2nd route to complete
        MockEndpoint mock = getMockEndpoint("mock:queue.order");
        mock.expectedBodiesReceived("amount=1,name=Camel in Action,id=123,status=OK");

        template.sendBodyAndHeader("file://target/orders", "amount=1#name=Camel in Action", Exchange.FILE_NAME, "order.txt");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testOrderActiveMQ() throws Exception {
        // we expect the file to be converted to csv and routed to the 2nd route
        MockEndpoint file = getMockEndpoint("mock:file");
        file.expectedMessageCount(1);

        // we do not expect the 2nd route to complete
        MockEndpoint mock = getMockEndpoint("mock:queue.order");
        mock.expectedMessageCount(0);

        template.sendBodyAndHeader("file://target/orders", "amount=1#name=ActiveMQ in Action", Exchange.FILE_NAME, "order.txt");

        // wait 10 seconds to let this test run
        Thread.sleep(10000);

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testXmlOrderFail() throws Exception {
        // we do not expect the file to be converted to csv
        MockEndpoint file = getMockEndpoint("mock:file");
        file.expectedMessageCount(0);

        // and therefore no messages in the 2nd route
        MockEndpoint mock = getMockEndpoint("mock:queue.order");
        mock.expectedMessageCount(0);

        template.sendBodyAndHeader("file://target/orders", "<?xml version=\"1.0\"?><order>"
                + "<amount>1</amount><name>Camel in Action</name></order>", Exchange.FILE_NAME, "order2.xml");

        // wait 5 seconds to let this test run
        Thread.sleep(5000);

        assertMockEndpointsSatisfied();
    }

}
