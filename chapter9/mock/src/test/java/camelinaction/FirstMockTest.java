package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Our first unit test using the Mock component
 */
public class FirstMockTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // use the stub component as a simple in-memory queue
                // instead of having to embed ActiveMQ as a JMS broker

                // a simple route which listen on messages on a JMS topic
                from("stub:jms:topic:quote").to("mock:quote");
            }
        };
    }

    @Test
    public void testQuote() throws Exception {
        // get the mock endpoint
        MockEndpoint quote = getMockEndpoint("mock:quote");
        // set expectations such as 1 message should arrive
        quote.expectedMessageCount(1);

        // fire in a message to Camel
        template.sendBody("stub:jms:topic:quote", "Camel rocks");

        // verify the result
        quote.assertIsSatisfied();
    }

}
