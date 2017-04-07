package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class SimpleBuilderTest extends CamelTestSupport {

    @Test
    public void testSimpleBuilder() throws Exception {
        getMockEndpoint("mock:camelMessage").expectedMessageCount(2);
        getMockEndpoint("mock:noCamelMessage").expectedMessageCount(1);        
        
        template.sendBody("direct:start", "Camel Rocks!");
        template.sendBody("direct:start", "Camel in Action Rocks :-)");
        template.sendBody("direct:start", "I'm going to cause an Exception...");
        
        assertMockEndpointsSatisfied();
    }
    
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                onException(Exception.class)
                    .handled(true)
                    .to("mock:noCamelMessage");
                
                from("direct:start")
                    .process(new MyProcessor())
                    .to("mock:camelMessage");
            }
        };
    }
}
