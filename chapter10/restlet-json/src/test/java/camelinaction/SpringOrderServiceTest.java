package camelinaction;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringOrderServiceTest extends CamelSpringTestSupport {

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("camelinaction/SpringOrderServiceTest.xml");
    }

    @Test
    public void testCreateOrder() throws Exception {
        String json = "{\"partName\":\"motor\",\"amount\":1,\"customerName\":\"honda\"}";

        log.info("Sending order using json payload: {}", json);

        // use restlet component to send the order
        String id = template.requestBody("restlet:http://0.0.0.0:8080/orders?restletMethod=POST", json, String.class);
        assertNotNull(id);

        log.info("Created new order with id " + id);

        // should create a new order with id 1
        assertEquals("1", id);
    }

    @Test
    public void testCreateAndGetOrder() throws Exception {
        String json = "{\"partName\":\"motor\",\"amount\":1,\"customerName\":\"honda\"}";

        log.info("Sending order using json payload: {}", json);

        // use restlet component to send the order
        String id = template.requestBody("restlet:http://0.0.0.0:8080/orders?restletMethod=POST", json, String.class);
        assertNotNull(id);

        log.info("Created new order with id " + id);

        // should create a new order with id 1
        assertEquals("1", id);

        // use restlet component to get the order
        String response = template.requestBodyAndHeader("restlet:http://0.0.0.0:8080/orders/{id}?restletMethod=GET", null, "id", "1", String.class);
        log.info("Response: {}", response);
    }

}
