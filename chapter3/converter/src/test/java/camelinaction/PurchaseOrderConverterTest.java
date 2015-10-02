package camelinaction;

import junit.framework.TestCase;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class PurchaseOrderConverterTest extends TestCase {

    public void testPurchaseOrderConverter() throws Exception {
        CamelContext context = new DefaultCamelContext();
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start").convertBodyTo(PurchaseOrder.class);
            }
        });
        context.start();

        ProducerTemplate template = context.createProducerTemplate();

        byte[] data = "##START##AKC4433   179.95    3##END##".getBytes();
        PurchaseOrder order = template.requestBody("direct:start", data, PurchaseOrder.class);
        assertNotNull(order);

        System.out.println(order);

        assertEquals("AKC4433", order.getName());
        assertEquals("179.95", order.getPrice().toString());
        assertEquals(3, order.getAmount());
    }

}
