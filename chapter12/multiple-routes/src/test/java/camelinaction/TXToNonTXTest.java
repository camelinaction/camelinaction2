package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.SpringRouteBuilder;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TXToNonTXTest extends CamelSpringTestSupport {

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("spring-context.xml");
    }

    @Test
    public void testWithCamel() throws Exception {
        template.sendBody("activemq:queue:a", "Hi Camel");
        // Need to sleep a while to make the test passed
        Thread.sleep(2000);
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

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new SpringRouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("activemq:queue:a")
                    .transacted()
                    .to("direct:quote")
                    .to("activemq:queue:b");

                from("direct:quote")
                    .choice()
                        .when(body().contains("Camel"))
                            .transform(constant("Camel rocks"))
                        .when(body().contains("Donkey"))
                            .throwException(new IllegalArgumentException("Donkeys not allowed"))
                    .otherwise()
                        .transform(body().prepend("Hello "));
            }
        };
    }

}
