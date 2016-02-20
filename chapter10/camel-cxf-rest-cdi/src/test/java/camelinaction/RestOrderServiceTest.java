package camelinaction;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

// TODO: make this testing easier with camel-cdi, a new camel-test-cdi module is in the works
@Ignore
public class RestOrderServiceTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new OrderRoute();
    }

    @Test
    public void testGetOrder() throws Exception {
        // use restlet component to get the order
        String response = template.requestBodyAndHeader("restlet:http://localhost:8080/orders/{id}?restletMethod=GET", null, "id", "1", String.class);
        log.info("Response: {}", response);
    }

    @Test
    public void testCreateOrder() throws Exception {
        Order order = new Order();
        order.setAmount(1);
        order.setPartName("motor");
        order.setCustomerName("honda");

        // convert to XML which we support
        String xml = context.getTypeConverter().convertTo(String.class, order);

        log.info("Sending order using xml payload: {}", xml);

        // use restlet component to send the order
        String id = template.requestBodyAndHeader("restlet:http://localhost:8080/orders?restletMethod=POST", xml, Exchange.CONTENT_TYPE, "application/xml", String.class);
        assertNotNull(id);

        log.info("Created new order with id " + id);

        // should create a new order with id 1
        assertEquals("1", id);
    }

    @Test
    public void testCreateAndGetOrder() throws Exception {
        Order order = new Order();
        order.setAmount(1);
        order.setPartName("motor");
        order.setCustomerName("honda");

        // convert to XML which we support
        String xml = context.getTypeConverter().convertTo(String.class, order);

        log.info("Sending order using xml payload: {}", xml);

        // use restlet component to send the order
        String id = template.requestBodyAndHeader("restlet:http://localhost:8080/orders?restletMethod=POST", xml, Exchange.CONTENT_TYPE, "application/xml", String.class);
        assertNotNull(id);

        log.info("Created new order with id " + id);

        // should create a new order with id 1
        assertEquals("1", id);

        // use restlet component to get the order
        String response = template.requestBodyAndHeader("restlet:http://localhost:8080/orders/{id}?restletMethod=GET", null, "id", "1", String.class);
        log.info("Response: {}", response);
    }

}
