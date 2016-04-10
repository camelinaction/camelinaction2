package camelinaction;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class WeaveByIdTest extends CamelTestSupport {

    @Override
    public boolean isUseAdviceWith() {
        // remember to override this method and return true to tell Camel that we are using advice-with in the routes
        return true;
    }

    @Test
    public void testWeaveById() throws Exception {
        RouteDefinition route = context.getRouteDefinition("quotes");
        route.adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                // select the route node with the id=transform
                // and then replace it with the following route parts
                weaveById("transform").replace()
                    .transform().simple("${body.toUpperCase()}");

                // and add at the end of the route to route to this mock endpoint
                weaveAddLast().to("mock:result");
            }
        });

        context.start();

        // we have replaced the bean transformer call with a simple expression that
        // performs an upper case
        getMockEndpoint("mock:result").expectedBodiesReceived("HELLO CAMEL");

        template.sendBody("seda:quotes", "Hello Camel");

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("seda:quotes").routeId("quotes")
                    .bean("transformer").id("transform")
                        .to("seda:lower");
            }
        };
    }
}
