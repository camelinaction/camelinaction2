package camelinaction;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.Test;

public class BlueprintRestBeanOrderServiceTest extends CamelBlueprintTestSupport {

    @Override
    protected String getBlueprintDescriptor() {
        return "OSGI-INF/blueprint/rest-order.xml";
    }

    @Test
    public void testGetOrder() throws Exception {
        DummyOrderService orderService = context().getRegistry().lookupByNameAndType("orderService", DummyOrderService.class);

        // setup some pre-existing orders
        orderService.setupDummyOrders();

        // use restlet component to get the order
        String response = template.requestBodyAndHeader("restlet:http://localhost:8080/orders/1?restletMethod=GET", null, "Accept", "application/json", String.class);
        log.info("Response: {}", response);
    }

    @Test
    public void testCreateOrder() throws Exception {
        String json = "{\"partName\":\"motor\",\"amount\":1,\"customerName\":\"honda\"}";

        log.info("Sending order using json payload: {}", json);

        // use restlet component to send the order
        Map headers = new HashMap();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        String id = template.requestBodyAndHeaders("restlet:http://localhost:8080/orders?restletMethod=POST", json, headers, String.class);
        assertNotNull(id);

        log.info("Created new order with id " + id);

        // should create a new order with id 3
        assertEquals("3", id);
    }

    @Test
    public void testCreateAndGetOrder() throws Exception {
        String json = "{\"partName\":\"motor\",\"amount\":1,\"customerName\":\"honda\"}";

        log.info("Sending order using json payload: {}", json);

        // use restlet component to send the order
        Map headers = new HashMap();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        String id = template.requestBodyAndHeaders("restlet:http://localhost:8080/orders?restletMethod=POST", json, headers, String.class);
        assertNotNull(id);

        log.info("Created new order with id " + id);

        // should create a new order with id 3
        assertEquals("3", id);

        // use restlet component to get the order
        String response = template.requestBodyAndHeader("restlet:http://localhost:8080/orders/1?restletMethod=GET", null, "Accept", "application/json", String.class);
        log.info("Response: {}", response);
    }

}
