package camelinaction;

import camelinaction.goal.GoalRouteBuilder;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeEventType;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import org.apache.camel.CamelContext;
import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * Vert.x verticle for live scores.
 */
public class LiveScoreVerticle extends AbstractVerticle {

    private CamelContext camelContext;
    private FluentProducerTemplate template;

    @Override
    public void start() throws Exception {

        // create a vertx router to setup websocket and http server
        Router router = Router.router(vertx);

        // configure allowed inbound and outbound traffics
        BridgeOptions options = new BridgeOptions()
                .addInboundPermitted(new PermittedOptions().setAddress("control"))
                .addOutboundPermitted(new PermittedOptions().setAddress("clock"))
                .addOutboundPermitted(new PermittedOptions().setAddress("games"))
                .addOutboundPermitted(new PermittedOptions().setAddress("goals"));

        // route websocket to vertx
        router.route("/eventbus/*").handler(SockJSHandler.create(vertx).bridge(options, event -> {
            if (event.type() == BridgeEventType.SOCKET_CREATED) {
                System.out.println("Websocket connection created");

                // a new client connected so setup its screen with the list of games
                vertx.setTimer(100, h -> template.to("direct:init-games").send());

            } else if (event.type() == BridgeEventType.SOCKET_CLOSED) {
                System.out.println("Websocket connection closed");
            }

            event.complete(true);
        }));

        // serve the static resources (src/main/resources/webroot)
        router.route().handler(StaticHandler.create());

        // let router accept on port 8080
        System.out.println("Listening on http://localhost:8080");
        vertx.createHttpServer().requestHandler(router::accept).listen(8080);

        // setup Camel to stream live scores
        camelContext = new DefaultCamelContext();
        camelContext.addRoutes(new GoalRouteBuilder(vertx));
        template = camelContext.createFluentProducerTemplate();
        camelContext.start();
    }

    @Override
    public void stop() throws Exception {
        template.stop();
        camelContext.stop();
    }

}
