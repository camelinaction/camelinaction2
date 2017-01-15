package camelinaction;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Unit test how to use Tracer with Spring XML.
 */
public class TracerSpringTest extends CamelSpringTestSupport {

    @Override
    public void setUp() throws Exception {
        deleteDirectory("target/rider/orders");
        super.setUp();
    }

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("camelinaction/TracerSpringTest.xml");
    }

    @Test
    public void testTracer() throws Exception {
        template.sendBody("file://target/rider/orders", "123,4444,20160810,222,1");

        String xml = consumer.receiveBody("seda:queue:orders", 5000, String.class);
        assertEquals("<order><id>123/id><customerId>4444/customerId><date>20160810</date>"
                + "<item><id>222</id><amount>1</amount></itemn></order>", xml);
    }

}
