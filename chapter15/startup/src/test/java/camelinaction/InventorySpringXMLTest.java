package camelinaction;

import camelinaction.inventory.UpdateInventoryInput;
import camelinaction.inventory.UpdateInventoryOutput;
import org.apache.camel.Exchange;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class InventorySpringXMLTest extends CamelSpringTestSupport {

    @Override
    public void setUp() throws Exception {
        deleteDirectory("target/inventory/updates");
        super.setUp();
    }

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/camel-route-xml.xml");
    }

    @Test
    public void testSingleWebservice() throws Exception {
        // create input object to send as webservice
        UpdateInventoryInput input = new UpdateInventoryInput();
        input.setSupplierId("4444");
        input.setPartId("57123");
        input.setName("Bumper");
        input.setAmount("50");

        // send the webservice and expect an OK reply
        UpdateInventoryOutput reply = template.requestBody("cxf:bean:inventoryEndpoint", input, UpdateInventoryOutput.class);
        assertEquals("OK", reply.getCode());
    }

    @Test
    public void testSingleFile() throws Exception {
        String input = "4444,57123,Bumper,50\n4444,57124,Fender,87";
        template.sendBodyAndHeader("file:target/inventory/updates", input, Exchange.FILE_NAME, "acme-1.csv");

        Thread.sleep(3000);
    }

}
