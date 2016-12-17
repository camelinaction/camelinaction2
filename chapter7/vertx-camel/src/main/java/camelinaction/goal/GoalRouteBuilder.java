package camelinaction.goal;

import io.vertx.core.Vertx;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.vertx.VertxComponent;

public class GoalRouteBuilder extends RouteBuilder {

    private final Vertx vertx;

    public GoalRouteBuilder(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void configure() throws Exception {
        // use vertx instance on the Camel vertx component
        VertxComponent vc = getContext().getComponent("vertx", VertxComponent.class);
        vc.setVertx(vertx);

        // the route for handling live score updates from the goal
        // component which is published to vertx addresses
        from("goal:goals.csv").routeId("livescore").autoStartup(false)
            .log("Goal event: ${header.action} -> ${body}")
            .choice()
                .when(simple("${header.action} == 'clock'"))
                    .to("vertx:clock")
                .when(simple("${header.action} == 'goal'"))
                    .to("vertx:goals");

        // consume from vertx control address when the user clicks the control buttons
        // then we want to start/suspend the livescore route accordingly
        from("vertx:control").routeId("control")
            .log("Control event: ${body}")
            .toD("controlbus:route?routeId=livescore&action=${body}");

    }
}
