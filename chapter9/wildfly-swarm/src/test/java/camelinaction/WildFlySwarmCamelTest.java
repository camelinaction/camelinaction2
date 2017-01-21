package camelinaction;

import javax.inject.Inject;
import javax.naming.InitialContext;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.cdi.Uri;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.swarm.arquillian.DefaultDeployment;

import static org.junit.Assert.assertEquals;

/**
 * Our first unit test with the Camel Test Kit.
 * We test the Hello World example of integration kits, which is moving a file.
 */
@RunWith(Arquillian.class)
@DefaultDeployment
public class WildFlySwarmCamelTest {

    @ArquillianResource
    InitialContext context;

    @Inject
    private CamelContext camelContext;

    @Inject @Uri("seda:inbox")
    private ProducerTemplate template;

    @Test
    public void testSeda() throws Exception {
        // send to the seda inbox queue
        template.sendBody("Hello Swarm");

        // receive from the seda outbox queue using a consumer template
        ConsumerTemplate consumer = camelContext.createConsumerTemplate();

        // use 5 second timeout
        Object body = consumer.receiveBody("seda:outbox", 5000);

        // expect it was the message we sent
        assertEquals("Hello Swarm", body);
    }

}
