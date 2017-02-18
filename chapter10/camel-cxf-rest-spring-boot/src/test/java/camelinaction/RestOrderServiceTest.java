package camelinaction;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest(classes = OrderApplication.class)
public class RestOrderServiceTest extends Assert {

    private static final Logger LOG = LoggerFactory.getLogger(RestOrderServiceTest.class);

    @Autowired
    private CamelContext context;

    @Autowired
    private ProducerTemplate template;

    @Test
    public void testGetOrder() throws Exception {
        DummyOrderService orderService = context.getRegistry().lookupByNameAndType("orderService", DummyOrderService.class);

        // setup some pre-existing orders
        orderService.setupDummyOrders();

        // use restlet component to get the order
        String response = template.requestBodyAndHeader("restlet:http://localhost:8080/orders/1?restletMethod=GET", null, "Accept", "application/json", String.class);
        LOG.info("Response: {}", response);
    }

    @Test
    public void testCreateOrder() throws Exception {
        String json = "{\"partName\":\"motor\",\"amount\":1,\"customerName\":\"honda\"}";

        LOG.info("Sending order using json payload: {}", json);

        // use restlet component to send the order
        Map headers = new HashMap();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        String id = template.requestBodyAndHeaders("restlet:http://localhost:8080/orders?restletMethod=POST", json, headers, String.class);
        assertNotNull(id);

        LOG.info("Created new order with id " + id);
    }

    @Test
    public void testCreateAndGetOrder() throws Exception {
        String json = "{\"partName\":\"motor\",\"amount\":1,\"customerName\":\"honda\"}";

        LOG.info("Sending order using json payload: {}", json);

        // use restlet component to send the order
        Map headers = new HashMap();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        String id = template.requestBodyAndHeaders("restlet:http://localhost:8080/orders?restletMethod=POST", json, headers, String.class);
        assertNotNull(id);

        LOG.info("Created new order with id " + id);

        // use restlet component to get the order
        String response = template.requestBodyAndHeader("restlet:http://localhost:8080/orders/" + id + "?restletMethod=GET", null, "Accept", "application/json", String.class);
        LOG.info("Response: {}", response);
    }

}
