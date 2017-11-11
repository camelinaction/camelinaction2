package camelinaction;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.util.StringHelper;
import org.junit.Test;

/**
 * Unit test to simulate a real component by mocking a TCP server called miranda.
 * <p/>
 * Instead using mock we can return a canned reply acting as if we was communicating
 * with the real component.
 */
public class MirandaTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("jetty://http://localhost:9080/service/order")
                    .process(new OrderQueryProcessor())
                    .to("mock:miranda")
                    .process(new OrderResponseProcessor());
            }
        };
    }

    @Test
    public void testMiranda() throws Exception {
        context.setTracing(true);

        MockEndpoint mock = getMockEndpoint("mock:miranda");
        mock.expectedBodiesReceived("ID=123");
        mock.whenAnyExchangeReceived(new Processor() {
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setBody("ID=123,STATUS=IN PROGRESS");
            }
        });

        String out = template.requestBody("http://localhost:9080/service/order?id=123", null, String.class);
        assertEquals("IN PROGRESS", out);

        assertMockEndpointsSatisfied();
    }

    private class OrderQueryProcessor implements Processor {

        public void process(Exchange exchange) throws Exception {
            String id = exchange.getIn().getHeader("id", String.class);
            exchange.getIn().setBody("ID=" + id);
        }

    }

    private class OrderResponseProcessor implements Processor {

        public void process(Exchange exchange) throws Exception {
            String body = exchange.getIn().getBody(String.class);
            String reply = StringHelper.after(body, "STATUS=");
            exchange.getIn().setBody(reply);
        }

    }

}
