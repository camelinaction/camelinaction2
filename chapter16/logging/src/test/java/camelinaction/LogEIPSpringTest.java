package camelinaction;

import org.apache.camel.Exchange;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Unit test to demonstrate using the Log EIP for custom logging using Spring XML
 */
public class LogEIPSpringTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("camelinaction/LogEIPSpringTest.xml");
    }

    @Override
    public void setUp() throws Exception {
        deleteDirectory("target/rider/orders");
        super.setUp();
    }

    @Test
    public void testLogEIP() throws Exception {
        template.sendBodyAndHeader("file://target/rider/orders", "123,4444,20170810,222,1", Exchange.FILE_NAME, "someorder.csv");

        String xml = consumer.receiveBody("seda:queue:orders", 5000, String.class);
        assertEquals("<order><id>123/id><customerId>4444/customerId><date>20170810</date>"
                + "<item><id>222</id><amount>1</amount></itemn></order>", xml);
    }

}
