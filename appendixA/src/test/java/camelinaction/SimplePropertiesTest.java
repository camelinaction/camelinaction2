package camelinaction;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class SimplePropertiesTest extends CamelTestSupport {

    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = super.createCamelContext();

        PropertiesComponent pc = new PropertiesComponent();
        pc.setLocations(new String[]{"camel.properties"});
        context.addComponent("properties", pc);

        return context;
    }
    
    @Test
    public void testSimpleProperties() throws Exception {
        getMockEndpoint("mock:end").expectedBodiesReceived("Camel Rocks!");
        
        template.sendBody("direct:start", "Camel");
        
        assertMockEndpointsSatisfied();
    }
    
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {        
                from("direct:start")
                    .setBody().simple("${body} ${properties:message}")
                    .to("mock:end");
            }
        };
    }
}
