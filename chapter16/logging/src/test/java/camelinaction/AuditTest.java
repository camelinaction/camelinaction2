package camelinaction;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Unit test to demonstrate using the wireTap EIP for audit logging
 */
public class AuditTest extends CamelTestSupport {

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
    public void testAudit() throws Exception {
        template.sendBody("file://target/rider/orders", "123,4444,20170810,222,1");

        String xml = consumer.receiveBody("jms:queue:orders", 5000, String.class);
        assertEquals("<order><id>123/id><customerId>4444/customerId><date>20170810</date>"
                + "<item><id>222</id><amount>1</amount></itemn></order>", xml);
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("file://target/rider/orders")
                        // load file into memory (using convertBodyTo to String) as we want to wire tap the file
                        .convertBodyTo(String.class)
                        .wireTap("seda:audit")
                        .bean(OrderCsvToXmlBean.class)
                        .to("jms:queue:orders");

                from("seda:audit")
                        .bean(AuditService.class, "auditFile");
            }
        };
    }
}
