package camelinaction;

import io.vertx.core.Vertx;
import org.apache.camel.builder.RouteBuilder;

/**
 * The Camel routes for the live score application.
 * <p/>
 * This route uses seda to route between Camel and Vert.X.
 * See how CamelBridgeOptions is configured in the Verticle class.
 */
public class LiveScoreRouteBuilder extends RouteBuilder {

    private final Vertx vertx;

    public LiveScoreRouteBuilder(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void configure() throws Exception {
        // initialize the list of games which is called when a new client connects to Vert.X backend
        from("direct:init-games").routeId("init-games")
            .log("Init games event")
            .to("goal:games.csv")
            // the frontend expect one message per game so split
            .split(body())
                .to("seda:games");

        // the route for handling live score updates from the goal
        // component which is published to vertx addresses
        from("goal:goals.csv").routeId("livescore").autoStartup(false)
            .log("Goal event: ${header.action} -> ${body}")
            .choice()
                .when(header("action").isEqualTo("clock"))
                    .to("seda:clock")
                .when(header("action").isEqualTo("goal"))
                    .to("seda:goals");

        // consume from vertx control address when the user clicks the control buttons
        // then we want to start/suspend the livescore route accordingly
        from("seda:control").routeId("control")
            .log("Control event: ${body}")
            .toD("controlbus:route?routeId=livescore&async=true&action=${body}");
    }

}
