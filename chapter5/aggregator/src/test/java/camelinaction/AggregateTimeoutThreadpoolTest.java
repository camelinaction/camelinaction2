package camelinaction;

import java.util.concurrent.ScheduledExecutorService;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * See the class {@link MyAggregationStrategy} for how the messages
 * are actually aggregated together.
 *
 * @see MyAggregationStrategy
 */
public class AggregateTimeoutThreadpoolTest extends CamelTestSupport {

    @Test
    public void testXML() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMessageCount(2);

        template.sendBody("direct:start", "<order name=\"motor\" amount=\"1000\" customer=\"honda\"/>");
        template.sendBody("direct:start", "<order name=\"motor\" amount=\"500\" customer=\"toyota\"/>");
        template.sendBody("direct:start", "<order name=\"gearbox\" amount=\"200\" customer=\"toyota\"/>");

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                ScheduledExecutorService threadPool = context.getExecutorServiceManager().newScheduledThreadPool(this, "MyThreadPool", 2);

                from("direct:start")
                    .log("Sending ${body}")
                    // aggregate based on xpath expression which extracts from the
                    // arrived message body.
                    // use class MyAggregationStrategy for aggregation
                    .aggregate(xpath("/order/@customer"), new MyAggregationStrategy())
                        // complete either when we have 2 messages or after 5 sec timeout
                        .completionSize(2).completionTimeout(5000)
                        .timeoutCheckerExecutorService(threadPool)
                        // do a little logging for the published message
                        .log("Completed by ${exchangeProperty.CamelAggregatedCompletedBy}")
                        .log("Sending out ${body}")
                        // and send it to the mock
                        .to("mock:result");
            }
         };
    }
}
