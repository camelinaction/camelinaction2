package camelinaction;

import java.io.IOException;
import java.net.ConnectException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Unit test simulating an error using a mock
 */
public class SimulateErrorUsingMockTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                context.setTracing(true);

                errorHandler(defaultErrorHandler()
                        .maximumRedeliveries(5).redeliveryDelay(1000));

                onException(IOException.class).maximumRedeliveries(3)
                        .handled(true)
                        .to("mock:ftp");

                from("direct:file")
                        .to("mock:http");
            }
        };
    }

    @Test
    public void testSimulateErrorUsingMock() throws Exception {
        getMockEndpoint("mock:ftp").expectedBodiesReceived("Camel rocks");

        MockEndpoint http = getMockEndpoint("mock:http");
        http.whenAnyExchangeReceived(new Processor() {
            public void process(Exchange exchange) throws Exception {
                exchange.setException(new ConnectException("Simulated connection error"));
            }
        });

        template.sendBody("direct:file", "Camel rocks");

        assertMockEndpointsSatisfied();
    }

}
