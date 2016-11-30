package camelinaction;

import java.util.concurrent.atomic.AtomicInteger;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

public class GoalServer extends AbstractVerticle {

    @Override
    public void start() throws Exception {

        Router router = Router.router(vertx);

        // Allow outbound traffic to the goals address
        BridgeOptions options = new BridgeOptions().addOutboundPermitted(new PermittedOptions().setAddress("goals"));

        router.route("/eventbus/*").handler(SockJSHandler.create(vertx).bridge(options, event -> {
            event.complete(true);
        }));

        // Serve the static resources
        router.route().handler(StaticHandler.create());

        // let router accept on port 8080
        vertx.createHttpServer().requestHandler(router::accept).listen(8080);

        final AtomicInteger goals = new AtomicInteger();

        // Publish a message to the address "goals" every second

        vertx.setPeriodic(10000, t -> vertx.eventBus().publish("goals", "Liverpool " + goals.incrementAndGet() + ",Everton 0,69,Fowler,Home"));
        vertx.setPeriodic(25000, t -> vertx.eventBus().publish("goals", "Liverpool " + goals.get() + ",Everton 1,88,Ferguson,Away"));
    }

}
