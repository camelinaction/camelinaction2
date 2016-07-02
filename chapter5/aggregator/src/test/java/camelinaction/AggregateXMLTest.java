package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * The XML example for using the Aggregator EIP.
 * <p/>
 * This example have XML messages send to the aggregator to demonstrate
 * using XPath for correlation expression. And we have two completion
 * conditions which is based on size and timeout
 * <p/>
 * See the class {@link MyAggregationStrategy} for how the messages
 * are actually aggregated together.
 *
 * @see MyAggregationStrategy
 */
public class AggregateXMLTest extends CamelTestSupport {

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
                from("direct:start")
                    .log("Sending ${body}")
                    // aggregate based on xpath expression which extracts from the
                    // arrived message body.
                    // use class MyAggregationStrategy for aggregation
                    .aggregate(xpath("/order/@customer"), new MyAggregationStrategy())
                        // complete either when we have 2 messages or after 5 sec timeout
                        .completionSize(2).completionTimeout(5000)
                        // do a little logging for the published message
                        .log("Completed by ${property.CamelAggregatedCompletedBy}")
                        .log("Sending out ${body}")
                        // and send it to the mock
                        .to("mock:result");
            }
        };
    }
}
