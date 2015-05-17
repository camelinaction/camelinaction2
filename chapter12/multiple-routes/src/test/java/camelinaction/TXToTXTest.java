package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TXToTXTest extends CamelSpringTestSupport {

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("spring-context.xml");
    }

    @Test
    public void testWithCamel() throws Exception {
        template.sendBody("activemq:queue:a", "Hi Camel");

        String reply = consumer.receiveBody("activemq:queue:b", 10000, String.class);
        assertEquals("Hi Camel", reply);

        reply = consumer.receiveBody("activemq:queue:camel", 10000, String.class);
        assertEquals("Hi Camel", reply);
    }

    @Test
    public void testWithDonkey() throws Exception {
        template.sendBody("activemq:queue:a", "Donkey");

        String reply = consumer.receiveBody("activemq:queue:b", 10000, String.class);
        assertNull("There should be no reply", reply);

        reply = consumer.receiveBody("activemq:queue:camel", 10000, String.class);
        assertNull("There should be no reply", reply);

        reply = consumer.receiveBody("activemq:queue:ActiveMQ.DLQ", 10000, String.class);
        assertNotNull("It should have been moved to DLQ", reply);
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("activemq:queue:a")
                    .transacted()
                    .to("direct:quote")
                    .to("activemq:queue:b");

                from("direct:quote")
                    .transacted()
                    .choice()
                        .when(body().contains("Camel"))
                            .to("activemq:queue:camel")
                    .otherwise()
                        .throwException(new IllegalArgumentException("Unsupported animal"));
            }
        };
    }

}
