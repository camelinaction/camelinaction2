package camelinaction;

import javax.inject.Inject;

import junit.framework.TestCase;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.cdi.CamelCdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(CamelCdiRunner.class)
public class OrderServiceTest extends TestCase {

    private static final Logger LOG = LoggerFactory.getLogger(OrderServiceTest.class);

    // authentication as jack when we call the rest service so we can access the secured service
    private String auth = "authMethod=Basic&authUsername=jack&authPassword=123";

    @Inject
    private CamelContext context;

    @Test
    public void testCreateOrder() throws Exception {
        ProducerTemplate template = context.createProducerTemplate();

        Order order = new Order();
        order.setAmount(1);
        order.setPartName("motor");
        order.setCustomerName("honda");

        // convert to XML which we support
        String xml = context.getTypeConverter().convertTo(String.class, order);

        LOG.info("Sending order using xml payload: {}", xml);

        // use http component to send the order
        String id = template.requestBody("http://localhost:8080/orders?" + auth, xml, String.class);
        assertNotNull(id);

        LOG.info("Created new order with id " + id);

        // should create a new order with id 3
        assertEquals("3", id);
    }

    @Test
    public void testCreateAndGetOrder() throws Exception {
        ProducerTemplate template = context.createProducerTemplate();

        Order order = new Order();
        order.setAmount(1);
        order.setPartName("motor");
        order.setCustomerName("honda");

        // convert to XML which we support
        String xml = context.getTypeConverter().convertTo(String.class, order);

        LOG.info("Sending order using xml payload: {}", xml);

        // use http component to send the order
        String id = template.requestBody("http://localhost:8080/orders?" + auth, xml, String.class);
        assertNotNull(id);

        LOG.info("Created new order with id " + id);

        // should create a new order with id 4 (as 3 was created in the previous test method)
        assertEquals("4", id);

        // use restlet component to get the order
        String response = template.requestBody("http://localhost:8080/orders/" + id + "?" + auth, null, String.class);
        LOG.info("Response: {}", response);
    }

}
