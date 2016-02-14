package camelinaction;

import org.apache.camel.Exchange;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringRestOrderServiceTest extends CamelSpringTestSupport {

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("camelinaction/SpringRestOrderServiceTest.xml");
    }

    @Test
    public void testGetOrder() throws Exception {
        DummyOrderService orderService = context().getRegistry().lookupByNameAndType("orderService", DummyOrderService.class);

        // setup some pre-existing orders
        orderService.setupDummyOrders();

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
