package camelinaction;

import io.vertx.core.Vertx;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.vertx.VertxComponent;

/**
 * The Camel routes for the live score application.
 * <p/>
 * This route uses Vert.X to route between Vert.X eventbus addresses and Camel endpoints.
 */
public class LiveScoreRouteBuilder extends RouteBuilder {

    private final Vertx vertx;

    public LiveScoreRouteBuilder(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void configure() throws Exception {
        // use vertx instance on the Camel vertx component
        getContext().getComponent("vertx", VertxComponent.class).setVertx(vertx);

        // initialize the list of games which is called when a new client connects to Vert.X backend
        from("direct:init-games").routeId("init-games")
            .log("Init games event")
            .to("goal:games.csv")
            // the frontend expect one message per game so split
            .split(body())
                .to("vertx:games");

        // the route for handling live score updates from the goal
        // component which is published to vertx addresses
        from("goal:goals.csv").routeId("livescore").autoStartup(false)
            .log("Goal event: ${header.action} -> ${body}")
            .choice()
                .when(header("action").isEqualTo("clock"))
                    .to("vertx:clock")
                .when(header("action").isEqualTo("goal"))
                    .to("vertx:goals");

        // consume from vertx control address when the user clicks the control buttons
        // then we want to start/suspend the livescore route accordingly
        from("vertx:control").routeId("control")
            .log("Control event: ${body}")
            .toD("controlbus:route?routeId=livescore&async=true&action=${body}");
    }

}
