package camelinaction;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Unit test how to use the Tracer.
 */
public class TracerTest extends CamelTestSupport {

    @Override
    public void setUp() throws Exception {
        deleteDirectory("target/rider/orders");
        super.setUp();
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = super.createCamelContext();
        // simulate JMS with the SEDA component
        context.addComponent("jms", context.getComponent("seda"));
        return context;
    }

    @Test
    public void testTracer() throws Exception {
        template.sendBody("file://target/rider/orders", "123,4444,20160810,222,1");

        String xml = consumer.receiveBody("jms:queue:orders", 5000, String.class);
        assertEquals("<order><id>123/id><customerId>4444/customerId><date>20160810</date>"
                + "<item><id>222</id><amount>1</amount></itemn></order>", xml);
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("file://target/rider/orders")
                        .tracing()
                        .wireTap("seda:audit")
                        .bean(OrderCsvToXmlBean.class)
                        .to("jms:queue:orders");

                from("seda:audit")
                        .bean(AuditService.class, "auditFile");
            }
        };
    }

}
