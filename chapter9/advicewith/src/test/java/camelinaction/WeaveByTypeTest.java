package camelinaction;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.SplitDefinition;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import static org.apache.camel.util.toolbox.AggregationStrategies.flexible;

public class WeaveByTypeTest extends CamelTestSupport {

    @Override
    public boolean isUseAdviceWith() {
        // remember to override this method and return true to tell Camel that we are using advice-with in the routes
        return true;
    }

    @Test
    public void testWeaveByType() throws Exception {
        RouteDefinition route = context.getRouteDefinition("quotes");
        route.adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                // find the splitter and insert the route snippet before it
                weaveByType(SplitDefinition.class)
                    .before()
                        .filter(body().contains("Donkey"))
                        .transform(simple("${body},Mules cannot do this"));
            }
        });

        context.start();

        getMockEndpoint("mock:line").expectedBodiesReceived("camel rules", "donkey is bad", "mules cannot do this");
        getMockEndpoint("mock:combined").expectedMessageCount(1);
        getMockEndpoint("mock:combined").message(0).body().isInstanceOf(List.class);

        template.sendBody("seda:quotes", "Camel Rules,Donkey is Bad");

        assertMockEndpointsSatisfied();

        resetMocks();

        // try again without the donkeys

        getMockEndpoint("mock:line").expectedBodiesReceived("beer is good", "whiskey is better");
        getMockEndpoint("mock:combined").expectedMessageCount(1);
        getMockEndpoint("mock:combined").message(0).body().isInstanceOf(List.class);

        template.sendBody("seda:quotes", "Beer is good,Whiskey is better");

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
                        .to("mock:line")
                    .end()
                    .to("mock:combined");
            }
        };
    }
}
