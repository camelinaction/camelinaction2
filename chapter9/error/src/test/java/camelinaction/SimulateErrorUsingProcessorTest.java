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
 * Unit test simulating an error using a Processor
 */
public class SimulateErrorUsingProcessorTest extends CamelTestSupport {

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
                        // simulate an error using a processor to throw the exception
                        .process(new Processor() {
                            public void process(Exchange exchange) throws Exception {
                                throw new ConnectException("Simulated connection error");
                            }
                        })
                        .to("mock:http");
            }
        };
    }

    @Test
    public void testSimulateErrorUsingProcessor() throws Exception {
        getMockEndpoint("mock:http").expectedMessageCount(0);

        MockEndpoint ftp = getMockEndpoint("mock:ftp");
        ftp.expectedBodiesReceived("Camel rocks");

        template.sendBody("direct:file", "Camel rocks");

        assertMockEndpointsSatisfied();
    }

}
