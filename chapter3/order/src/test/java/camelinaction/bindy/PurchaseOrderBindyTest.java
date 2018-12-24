package camelinaction.bindy;

import java.math.BigDecimal;
import java.util.Locale;

import junit.framework.TestCase;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.dataformat.BindyType;
import org.junit.Test;

public class PurchaseOrderBindyTest extends TestCase {

    private Locale locale;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // use US locale for testing so we use dot as decimal in the price
        locale = Locale.getDefault();
        Locale.setDefault(Locale.US);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        // restore back
        Locale.setDefault(locale);
    }

    @Test
    public void testBindy() throws Exception {
        CamelContext context = new DefaultCamelContext();
        context.addRoutes(createRoute());
        context.start();

        MockEndpoint mock = context.getEndpoint("mock:result", MockEndpoint.class);
        mock.expectedBodiesReceived("Camel in Action,69.99,1\n");

        PurchaseOrder order = new PurchaseOrder();
        order.setAmount(1);
        order.setPrice(new BigDecimal("69.99"));
        order.setName("Camel in Action");

        ProducerTemplate template = context.createProducerTemplate();
        template.sendBody("direct:toCsv", order);

        mock.assertIsSatisfied();
    }

    public RouteBuilder createRoute() {
        return new RouteBuilder() {
            public void configure() throws Exception {
                from("direct:toCsv")
                        .marshal().bindy(BindyType.Csv, camelinaction.bindy.PurchaseOrder.class)
                        .to("mock:result");
            }
        };
    }

}
