package camelinaction;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringOrderServiceTest extends CamelSpringTestSupport {

    // authentication as jack when we call the rest service so we can access the secured service
    private String auth = "authMethod=Basic&authUsername=harry&authPassword=456";

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("camelinaction/SpringOrderServiceTest.xml");
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

        // use http component to send the order
        String id = template.requestBody("http://localhost:8080/orders?" + auth, xml, String.class);
        assertNotNull(id);

        log.info("Created new order with id " + id);

        // should create a new order with id 3
        assertEquals("3", id);
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

        // use http component to send the order
        String id = template.requestBody("http://localhost:8080/orders?" + auth, xml, String.class);
        assertNotNull(id);

        log.info("Created new order with id " + id);

        // should create a new order with id 3
        assertEquals("3", id);

        // use restlet component to get the order
        String response = template.requestBody("http://localhost:8080/orders/" + id + "?" + auth, null, String.class);
        log.info("Response: {}", response);
    }

}
