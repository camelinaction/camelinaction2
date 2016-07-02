package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * The ABC example for using the Aggregator EIP.
 * <p/>
 * This example have 4 messages send to the aggregator, by which one
 * message is published which contains the aggregation of message 1,2 and 4
 * as they use the same correlation key.
 * <p/>
 * This time we use the completionPredicate to know when the END message arrives
 * and thus triggers completion. Notice how we use the eagerCheckCompletion which
 * cause the completionPredicate to have the arriving Exchange as input. If we <b>did not</b>
 * do that then the completionPredicate will have the aggregated Exchange as input.
 * <p/>
 * See the class {@link MyEndAggregationStrategy} for how the messages
 * are actually aggregated together. Notice how this class will discard the END message.
 *
 * @see MyEndAggregationStrategy
 */
public class AggregateABCEagerTest extends CamelTestSupport {

    @Test
    public void testABCEND() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        // we expect ABC in the published message
        // notice: Only 1 message is expected
        mock.expectedBodiesReceived("ABC");

        // send the first message
        template.sendBodyAndHeader("direct:start", "A", "myId", 1);
        // send the 2nd message with the same correlation key
        template.sendBodyAndHeader("direct:start", "B", "myId", 1);
        // the F message has another correlation key
        template.sendBodyAndHeader("direct:start", "F", "myId", 2);
        // now we have 3 messages with the same correlation key
        // and the Aggregator should publish the message
        template.sendBodyAndHeader("direct:start", "C", "myId", 1);
        // and now the END message to trigger completion
        template.sendBodyAndHeader("direct:start", "END", "myId", 1);

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                    // do a little logging
                    .log("Sending ${body} with correlation key ${header.myId}")
                    // aggregate based on header correlation key
                    // use class MyEndAggregationStrategy for aggregation (special for END)
                    // and complete when the END message arrives
                    .aggregate(header("myId"), new MyEndAggregationStrategy())
                        // must enable eager check to have the completion predicate
                        // to match when we received END in the arrived message body
                        .completionPredicate(body().isEqualTo("END")).eagerCheckCompletion()
                        // do a little logging for the published message
                        .log("Sending out ${body}")
                        // and send it to the mock
                        .to("mock:result");
            }
        };
    }
}
