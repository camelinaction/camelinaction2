package camelinaction;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Unit test demonstrating various functionality of using advice-with
 */
public class AdviceWithMockEndpointsTest extends CamelTestSupport {

    @Override
    public boolean isUseAdviceWith() {
        // remember to override this method and return true to tell Camel that we are using advice-with in the routes
        return true;
    }

    @Test
    public void testMockEndpoints() throws Exception {
        RouteDefinition route = context.getRouteDefinition("quotes");
        route.adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                mockEndpoints();
            }
        });

        // must start Camel after we are done using advice-with
        context.start();

        getMockEndpoint("mock:seda:camel").expectedBodiesReceived("Camel rocks");
        getMockEndpoint("mock:seda:other").expectedBodiesReceived("Bad donkey");

        template.sendBody("seda:quotes", "Camel rocks");
        template.sendBody("seda:quotes", "Bad donkey");

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("seda:quotes").routeId("quotes")
                    .choice()
                        .when(simple("${body} contains 'Camel'"))
                            .to("seda:camel")
                        .otherwise()
                            .to("seda:other");
            }
        };
    }
}
