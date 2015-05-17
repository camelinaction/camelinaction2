package camelinaction;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Showing how to reuse error handler from a base route builder class
 */
public class ReuseErrorHandlerTest extends CamelTestSupport {

    @Override
    public void setUp() throws Exception {
        deleteDirectory("target/orders");
        super.setUp();
    }

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        // register our order service bean in the Camel registry
        JndiRegistry jndi = super.createRegistry();
        jndi.bind("orderService", new OrderService());
        return jndi;
    }

    @Test
    public void testOrderOk() throws Exception {
        // we do not expect any errors and therefore no messages in the dead letter queue
        getMockEndpoint("mock:dead").expectedMessageCount(0);

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
    public void testOrderActiveMQFail() throws Exception {
        // we expect the order to fail and end up in the dead letter queue
        getMockEndpoint("mock:dead").expectedMessageCount(1);

        // we expect the file to be converted to csv and routed to the 2nd route
        MockEndpoint file = getMockEndpoint("mock:file");
        file.expectedMessageCount(1);

        // we do not expect the 2nd route to complete
        MockEndpoint mock = getMockEndpoint("mock:queue.order");
        mock.expectedMessageCount(0);

        template.sendBodyAndHeader("file://target/orders", "amount=1#name=ActiveMQ in Action", Exchange.FILE_NAME, "order.txt");

        // wait 5 seconds to let this test run
        Thread.sleep(5000);

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testXmlOrderFail() throws Exception {
        // we expect the order to fail and end up in the dead letter queue
        getMockEndpoint("mock:dead").expectedMessageCount(1);

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

    @Override
    protected RouteBuilder[] createRouteBuilders() throws Exception {
        // we use 2 route builders in this example, the inbox and order route builders
        return new RouteBuilder[]{new InboxRouteBuilder(), new OrderRouteBuilder()};
    }
}
