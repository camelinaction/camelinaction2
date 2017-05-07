package camelinaction;

import java.util.concurrent.TimeUnit;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.leveldb.LevelDBAggregationRepository;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * The ABC example for using the Aggregator EIP.
 * <p/>
 * This example uses recovery to have the published messages being redelivered
 * in case processing failed.
 * <p/>
 * See the class {@link MyAggregationStrategy} for how the messages
 * are actually aggregated together.
 *
 * @see MyAggregationStrategy
 */
public class AggregateABCRecoverTest extends CamelTestSupport {

    @Override
    public void setUp() throws Exception {
        // ensure we use a fresh repo each time by deleting the data directory
        deleteDirectory("data");
        super.setUp();
    }

    @Test
    public void testABCRecover() throws Exception {
        // we should never get a result
        getMockEndpoint("mock:result").expectedMessageCount(0);

        MockEndpoint mock = getMockEndpoint("mock:aggregate");
        // we should try 1 time + 4 times as redelivery
        mock.expectedMessageCount(5);

        MockEndpoint dead = getMockEndpoint("mock:dead");
        // we should send the message to dead letter channel when exhausted
        dead.expectedBodiesReceived("ABC");
        // should be marked as redelivered
        dead.message(0).header(Exchange.REDELIVERED).isEqualTo(true);
        // and we did try 4 times to redeliver
        dead.message(0).header(Exchange.REDELIVERY_COUNTER).isEqualTo(4);

        // send the first message
        template.sendBodyAndHeader("direct:start", "A", "myId", 1);
        // send the 2nd message with the same correlation key
        template.sendBodyAndHeader("direct:start", "B", "myId", 1);
        // now we have 3 messages with the same correlation key
        // and the Aggregator should publish the message
        template.sendBodyAndHeader("direct:start", "C", "myId", 1);

        // wait for 20 seconds as this test takes some time
        assertMockEndpointsSatisfied(20, TimeUnit.SECONDS);
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                LevelDBAggregationRepository levelDB = 
                    new LevelDBAggregationRepository("myrepo", "data/myrepo.dat");
                // will recover by default
                levelDB.setUseRecovery(true);
                // try at most 4 times
                levelDB.setMaximumRedeliveries(4);
                // send to mock:dead if exhausted
                levelDB.setDeadLetterUri("mock:dead");
                // have it retry every 3th second
                levelDB.setRecoveryInterval(3000);

                from("direct:start")
                    // do a little logging
                    .log("Sending ${body} with correlation key ${header.myId}")
                    // aggregate based on header correlation key
                    // use class MyAggregationStrategy for aggregation
                    // and complete when we have aggregated 3 messages
                    .aggregate(header("myId"), new MyAggregationStrategy())
                        // use LevelDB as the persistent repository
                        .aggregationRepository(levelDB)
                        // and complete when we got 3 messages
                        .completionSize(3)
                        // do a little logging for the published message
                        .log("Sending out ${body}")
                        // use a mock to check recovery
                        .to("mock:aggregate")
                        // force failure to have the message being recovered
                        .throwException(new IllegalArgumentException("Damn does not work"))
                        // and send it to the mock (not possible, due exception being thrown)
                        .to("mock:result");
            }
        };
    }
}
