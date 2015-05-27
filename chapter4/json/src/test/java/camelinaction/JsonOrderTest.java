package camelinaction;

import org.apache.camel.Exchange;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Test to demonstrate using @JsonPath and @Bean annotations in the {@link JSonOrderService} bean.
 */
public class JsonOrderTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("camelinaction/jsonOrder.xml");
    }

    @Override
    public void setUp() throws Exception {
        deleteDirectory("target/order");
        super.setUp();
    }

    @Test
    public void sendIncomingOrder() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:queue:order");
        mock.expectedMessageCount(1);

        // prepare a JSon document from a String
        String json = "{ \"order\": { \"customerId\": 4444, \"item\": \"Camel in Action\" } }";

        // store the order as a file which is picked up by the route
        template.sendBodyAndHeader("file://target/order", json, Exchange.FILE_NAME, "order.json");

        mock.assertIsSatisfied();
    }

}
