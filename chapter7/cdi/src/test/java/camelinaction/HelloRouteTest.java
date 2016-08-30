package camelinaction;

import javax.inject.Inject;

import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.cdi.Uri;
import org.apache.camel.test.cdi.CamelCdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

/**
 * Basic unit test with camel-cdi-test
 *
 * Use the CamelCdiRunner to perform CDI unit test
 */
@RunWith(CamelCdiRunner.class)
public class HelloRouteTest {

    // inject FluentProducerTemplate to make it easy
    // to send a message to the endpoint
    @Inject
    @Uri("jetty:http://localhost:8080/hello")
    private FluentProducerTemplate producer;

    @Test
    public void testHello() throws Exception {
        // call the url using the injected producer (with no body)
        String out = producer.request(String.class);
        // assert that the reply message is what we expect
        assertTrue(out.startsWith("Hello from Camel CDI with properties"));
    }

}
