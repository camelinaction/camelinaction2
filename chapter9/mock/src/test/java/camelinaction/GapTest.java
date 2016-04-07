package camelinaction;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Unit test showing how to use a custom expression. The custom expression detect gaps in a sequence
 * counter in the message arrived on the mock endpoint.
 */
public class GapTest extends CamelTestSupport {

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
    public void testGapDetected() throws Exception {
        final MockEndpoint mock = getMockEndpoint("mock:quote");
        // expect 3 messages
        mock.expectedMessageCount(3);

        // add our own expectation
        mock.expects(new Runnable() {
            public void run() {
                // loop the received exchanges and detect gaps
                int last = 0;
                for (Exchange exchange : mock.getExchanges()) {
                    // get the current index
                    int current = exchange.getIn().getHeader("Counter", Integer.class);
                    // must be greater than the last number
                    if (current <= last) {
                        fail("Counter is not greater than last counter");
                        // and the gap between must exactly be 1
                    } else if (current - last != 1) {
                        fail("Gap detected: last: " + last + " current: " + current);
                    }
                    // remember as new last
                    last = current;
                }
            }
        });

        // send in 3 messages where there is a gap
        template.sendBodyAndHeader("stub:jms:topic:quote", "A", "Counter", 1);
        template.sendBodyAndHeader("stub:jms:topic:quote", "B", "Counter", 2);
        template.sendBodyAndHeader("stub:jms:topic:quote", "C", "Counter", 4);

        // assert that fails since there was a gap
        try {
            assertMockEndpointsSatisfied();
        } catch (AssertionError e) {
            System.out.println(e.getMessage());
            assertEquals("Gap detected: last: 2 current: 4", e.getMessage());
        }
    }

    /**
     * Same as the unit test above but using mock to know it should fail
     */
    @Test
    public void testGapDetectedExpected() throws Exception {
        final MockEndpoint mock = getMockEndpoint("mock:quote");
        mock.expectedMessageCount(3);
        mock.expects(new Runnable() {
            public void run() {
                int last = 0;
                for (Exchange exchange : mock.getExchanges()) {
                    int current = exchange.getIn().getHeader("Counter", Integer.class);
                    if (current <= last) {
                        fail("Counter is not greater than last counter");
                    } else if (current - last != 1) {
                        fail("Gap detected: last: " + last + " current: " + current);
                    }
                    last = current;
                }
            }
        });

        template.sendBodyAndHeader("stub:jms:topic:quote", "A", "Counter", 1);
        template.sendBodyAndHeader("stub:jms:topic:quote", "B", "Counter", 2);
        template.sendBodyAndHeader("stub:jms:topic:quote", "C", "Counter", 4);

        // assert that we fail using Is_Not_Satisfied
        mock.assertIsNotSatisfied();
    }

}
