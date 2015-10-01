package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Demonstrates how to return an early reply to a waiting caller
 * while Camel can continue processing the received message afterwards.
 */
public class EarlyReplyTest extends CamelTestSupport {

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

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("jetty:http://localhost:8080/early").routeId("input")
                    // use wiretap to continue processing the message
                    // in another thread and let this consumer continue
                    .wireTap("direct:incoming")
                    // and return an early reply to the waiting caller
                    .transform().constant("OK");

                from("direct:incoming").routeId("process")
                    // convert the jetty stream to String so we can safely read it multiple times
                    .convertBodyTo(String.class)
                    .log("Incoming ${body}")
                    // simulate processing by delaying 3 seconds
                    .delay(3000)
                    .log("Processing done for ${body}")
                    .to("mock:result");
            }
        };
    }
}
