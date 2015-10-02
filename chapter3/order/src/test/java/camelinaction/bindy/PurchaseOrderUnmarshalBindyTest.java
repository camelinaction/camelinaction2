package camelinaction.bindy;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.dataformat.BindyType;
import org.junit.Test;

/**
 * Test that demonstrates how to turn a CSV into a Object using bindy
 */
public class PurchaseOrderUnmarshalBindyTest extends TestCase {

    @Test
    public void testUnmarshalBindyMultipleRows() throws Exception {
        CamelContext context = new DefaultCamelContext();
        context.addRoutes(createRoute());
        context.start();

        MockEndpoint mock = context.getEndpoint("mock:result", MockEndpoint.class);
        mock.expectedMessageCount(1);

        ProducerTemplate template = context.createProducerTemplate();
        template.sendBody("direct:toObject", "Camel in Action,39.95,1\nActiveMQ in Action,39.95,1");

        mock.assertIsSatisfied();

        // bindy now turned that into a list of rows so we need to grab the order from the list
        List rows = mock.getReceivedExchanges().get(0).getIn().getBody(List.class);
        PurchaseOrder order = (PurchaseOrder) rows.get(0);
        assertNotNull(order);
        PurchaseOrder order2 = (PurchaseOrder) rows.get(1);
        assertNotNull(order2);       
        
        // assert the order contains the expected data
        assertEquals("Camel in Action", order.getName());
        assertEquals("39.95", order.getPrice().toString());
        assertEquals(1, order.getAmount());
        assertEquals("ActiveMQ in Action", order2.getName());
        assertEquals("39.95", order2.getPrice().toString());
        assertEquals(1, order2.getAmount());
    }

    @Test
    public void testUnmarshalBindy() throws Exception {
        CamelContext context = new DefaultCamelContext();
        context.addRoutes(createRoute());
        context.start();

        MockEndpoint mock = context.getEndpoint("mock:result", MockEndpoint.class);
        mock.expectedMessageCount(1);

        ProducerTemplate template = context.createProducerTemplate();
        template.sendBody("direct:toObject", "Camel in Action,39.95,1");

        mock.assertIsSatisfied();

        // bindy returns the order directly (not in a list) if there is only one element
        PurchaseOrder order = mock.getReceivedExchanges().get(0).getIn().getBody(PurchaseOrder.class);
        assertNotNull(order);

        // assert the order contains the expected data
        assertEquals("Camel in Action", order.getName());
        assertEquals("39.95", order.getPrice().toString());
        assertEquals(1, order.getAmount());
    }
    
    public RouteBuilder createRoute() {
        return new RouteBuilder() {
            public void configure() throws Exception {
                from("direct:toObject")
                        .unmarshal().bindy(BindyType.Csv, camelinaction.bindy.PurchaseOrder.class)
                        .to("mock:result");
            }
        };
    }

}
