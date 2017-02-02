package camelinaction;

import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.cdi.Uri;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.swarm.arquillian.DefaultDeployment;

import static org.junit.Assert.assertEquals;

/**
 * Our first unit test with the Camel Test Kit.
 * We test the Hello World example of integration kits, which is moving a file.
 */
@RunWith(Arquillian.class)
@DefaultDeployment(type = DefaultDeployment.Type.JAR)
public class WildFlySwarmCamelTest {

    @Inject
    private CamelContext camelContext;

    @Inject @Uri("seda:inbox")
    private ProducerTemplate producer;

    @Test
    public void testSeda() throws Exception {
        // send to the seda inbox queue
        producer.sendBody("Hello Swarm");

        ConsumerTemplate consumer = camelContext.createConsumerTemplate();
        // use 5 second timeout to receive the message from outbox
        Object body = consumer.receiveBody("seda:outbox", 5000);

        // expect it was the message we sent
        assertEquals("Hello Swarm", body);
    }

}
