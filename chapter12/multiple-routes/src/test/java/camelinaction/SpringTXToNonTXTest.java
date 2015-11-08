package camelinaction;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringTXToNonTXTest extends CamelSpringTestSupport {

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("TXToNonTXTest.xml");
    }

    @Test
    public void testWithCamel() throws Exception {
        template.sendBody("activemq:queue:a", "Hi Camel");

        String reply = consumer.receiveBody("activemq:queue:b", 10000, String.class);
        assertEquals("Camel rocks", reply);
    }

    @Test
    public void testWithOther() throws Exception {
        template.sendBody("activemq:queue:a", "Superman");

        String reply = consumer.receiveBody("activemq:queue:b", 10000, String.class);
        assertEquals("Hello Superman", reply);
    }

    @Test
    public void testWithDonkey() throws Exception {
        template.sendBody("activemq:queue:a", "Donkey");

        String reply = consumer.receiveBody("activemq:queue:b", 10000, String.class);
        assertNull("There should be no reply", reply);

        reply = consumer.receiveBody("activemq:queue:ActiveMQ.DLQ", 10000, String.class);
        assertNotNull("It should have been moved to DLQ", reply);
    }

}
