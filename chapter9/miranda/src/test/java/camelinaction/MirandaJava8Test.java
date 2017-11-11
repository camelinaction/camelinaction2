package camelinaction;

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
 * <p/>
 * This test uses Java 8 DSL instead of Camel {@link Processor}s.
 */
public class MirandaJava8Test extends CamelTestSupport {

    @Test
    public void testMiranda() throws Exception {
        context.setTracing(true);

        MockEndpoint mock = getMockEndpoint("mock:miranda");
        mock.expectedBodiesReceived("ID=123");
        mock.whenAnyExchangeReceived(e -> e.getIn().setBody("ID=123,STATUS=IN PROGRESS"));

        String out = fluentTemplate.to("http://localhost:9080/service/order?id=123").request(String.class);
        assertEquals("IN PROGRESS", out);

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("jetty://http://localhost:9080/service/order")
                    .transform().message(m -> "ID=" + m.getHeader("id"))
                    .to("mock:miranda")
                    .transform().body(String.class, b -> StringHelper.after(b, "STATUS="));
            }
        };
    }

}
