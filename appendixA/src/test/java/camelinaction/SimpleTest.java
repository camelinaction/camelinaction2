package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class SimpleTest extends CamelTestSupport {

    @Test
    public void testSimpleProperties() throws Exception {
        getMockEndpoint("mock:camel").expectedBodiesReceived("Camel Rocks!");
        getMockEndpoint("mock:bigspender").expectedBodiesReceived("Big spender");
        getMockEndpoint("mock:other").expectedBodiesReceived("Other");
        
        template.sendBody("direct:start", "Camel Rocks!");
        template.sendBodyAndHeader("direct:start", "Big spender", "amount", 1001);
        template.sendBody("direct:start", "Other");
        
        assertMockEndpointsSatisfied();
    }
    
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {        
                from("direct:start")
                .choice()
                    .when(simple("${body} contains 'Camel'")).to("mock:camel")
                    .when(simple("${header.amount} > 1000")).to("mock:bigspender")
                    .otherwise().to("mock:other");
            }
        };
    }
}
