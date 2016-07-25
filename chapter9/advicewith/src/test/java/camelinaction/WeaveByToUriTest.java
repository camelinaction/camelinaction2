package camelinaction;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.ToDefinition;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import static org.apache.camel.util.toolbox.AggregationStrategies.flexible;

public class WeaveByToUriTest extends CamelTestSupport {

    @Override
    public boolean isUseAdviceWith() {
        // remember to override this method and return true to tell Camel that we are using advice-with in the routes
        return true;
    }

    @Test
    public void testWeaveByToUri() throws Exception {
        RouteDefinition route = context.getRouteDefinition("quotes");
        route.adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                // replace all to("seda:line") with a mock:line instead
                weaveByToUri("seda:line").replace().to("mock:line");
            }
        });

        context.start();

        getMockEndpoint("mock:line").expectedBodiesReceived("camel rules", "donkey is bad");
        getMockEndpoint("mock:combined").expectedMessageCount(1);
        getMockEndpoint("mock:combined").message(0).body().isInstanceOf(List.class);

        template.sendBody("seda:quotes", "Camel Rules,Donkey is Bad");

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("seda:quotes").routeId("quotes")
                    .split(body(), flexible().accumulateInCollection(ArrayList.class))
                        .transform(simple("${body.toLowerCase()}"))
                        .to("seda:line")
                    .end()
                    .to("mock:combined");
            }
        };
    }
}
