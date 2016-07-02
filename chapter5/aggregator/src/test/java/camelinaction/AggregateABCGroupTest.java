package camelinaction;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.processor.aggregate.GroupedExchangeAggregationStrategy;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * The ABC example for using the Aggregator EIP.
 * <p/>
 * This example have 4 messages send to the aggregator, by which one
 * message is published which groups the incoming messages.
 * <p/>
 * And this time we group the incoming messages which means all 4 incoming Exchange
 * is kept in a List on the published Exchange. The List is stored as a property
 * which allows you to get access to those original incoming Exchanges.
 */
public class AggregateABCGroupTest extends CamelTestSupport {

    @SuppressWarnings("unchecked")
	@Test
    public void testABCGroup() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        // one message expected
        mock.expectedMessageCount(1);
        // As the fix of CAMEL-6557, the message body is not empty anymore
        // should not have a body
        //mock.message(0).body().isNull();
        // but have it stored in a property as a List
        mock.message(0).exchangeProperty(Exchange.GROUPED_EXCHANGE).isInstanceOf(List.class);

        template.sendBodyAndHeader("direct:start", "A", "myId", 1);
        template.sendBodyAndHeader("direct:start", "B", "myId", 1);
        template.sendBodyAndHeader("direct:start", "F", "myId", 2);
        template.sendBodyAndHeader("direct:start", "C", "myId", 1);

        assertMockEndpointsSatisfied();

        // get the published exchange
        Exchange exchange = mock.getExchanges().get(0);

        // retrieve the List which contains the arrived exchanges
        List list = exchange.getProperty(Exchange.GROUPED_EXCHANGE, List.class);
        assertEquals("Should contain the 3 arrived exchanges", 3, list.size());

        // assert the 3 exchanges are in order and contains the correct body
        Exchange a = (Exchange) list.get(0);
        assertEquals("A", a.getIn().getBody());

        Exchange b = (Exchange) list.get(1);
        assertEquals("B", b.getIn().getBody());

        Exchange c = (Exchange) list.get(2);
        assertEquals("C", c.getIn().getBody());
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
                    .aggregate(header("myId"), new GroupedExchangeAggregationStrategy()).completionSize(3)
                        // do a little logging for the published message
                        .log("Sending out ${body}")
                        // and send it to the mock
                        .to("mock:result");
            }
        };
    }
}
