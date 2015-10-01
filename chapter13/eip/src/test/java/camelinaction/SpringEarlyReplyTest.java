package camelinaction;

import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Demonstrates how to return an early reply to a waiting caller
 * while Camel can continue processing the received message afterwards.
 */
public class SpringEarlyReplyTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/earlyreply-context.xml");
    }

    @Test
    public void testEarlyReply() throws Exception {
        final String body = "Hello Camel";

        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedBodiesReceived(body);

        // send an InOut (= requestBody) to Camel
        log.info("Caller calling Camel with message: " + body);
        String reply = template.requestBody("jetty:http://localhost:8080/early", body, String.class);

        // we should get the reply early which means you should see this log line
        // before Camel has finished processed the message
        log.info("Caller finished calling Camel and received reply: " + reply);
        assertEquals("OK", reply);

        assertMockEndpointsSatisfied();
    }

}
