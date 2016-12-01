package camelinaction;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeEventType;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import org.apache.camel.util.IOHelper;

/**
 * Vert.x verticle for live scores.
 */
public class LiveScoreVerticle extends AbstractVerticle {

    // TODO: add button to start clock / reset clock
    // TODO: add documentation
    // TODO: add readme file
    // TODO: add vertx-camel bridge to make Camel emit the goal events
    // TODO: add ui option to turn on/off fast mode

    // to use fast mode where each second is a minute
    private boolean fastMode = true;

    private final AtomicInteger gameTime = new AtomicInteger();

    @Override
    public void start() throws Exception {

        // create a vertx router to setup websocket and http server
        Router router = Router.router(vertx);

        // allow outbound traffic to the games/goals address
        BridgeOptions options = new BridgeOptions()
                .addOutboundPermitted(new PermittedOptions().setAddress("clock"))
                .addOutboundPermitted(new PermittedOptions().setAddress("games"))
                .addOutboundPermitted(new PermittedOptions().setAddress("goals"));

        // route websocket to vertx
        router.route("/eventbus/*").handler(SockJSHandler.create(vertx).bridge(options, event -> {
            if (event.type() == BridgeEventType.SOCKET_CREATED) {
                System.out.println("Websocket connection created");

                // a new client connected so setup its screen with the list of games
                vertx.setTimer(500, h -> initGames());

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

        streamLiveScore();
    }

    private void initGames() {
        InputStream is = LiveScoreVerticle.class.getClassLoader().getResourceAsStream("games.csv");

        try {
            String text = IOHelper.loadText(is);
            String[] lines = text.split("\n");
            for (String line : lines) {
                vertx.eventBus().publish("games", line);
            }
        } catch (IOException e) {
            System.out.println("Error reading games.csv file due " + e.getMessage());
        }

        vertx.eventBus().publish("clock", "" + gameTime.get());
    }

    private void streamLiveScore() {
        List<String> lines = new ArrayList<>();

        InputStream is = LiveScoreVerticle.class.getClassLoader().getResourceAsStream("goals.csv");
        try {
            String text = IOHelper.loadText(is);
            String[] row = text.split("\n");
            lines.addAll(Arrays.asList(row));

            // sort on minutes
            lines.sort((a, b) -> goalTime(a).compareTo(goalTime(b)));
        } catch (IOException e) {
            System.out.println("Error reading goals.csv file due " + e.getMessage());
            return;
        }

        int time = fastMode ? 5 * 1000 : 60 * 1000;

        System.out.println("Publishing game clock");
        vertx.setPeriodic(time, event -> {
            int min = gameTime.incrementAndGet();
            if (min > 91) {
                return;
            }

            System.out.println("Game time " + min);

            // publish game time
            vertx.eventBus().publish("clock", String.valueOf(min));
        });

        System.out.println("Publishing live score");
        vertx.setPeriodic(time, event -> {
            int min = gameTime.get();
            if (min > 91) {
                return;
            }

            // stream all goals for the current game time
            List<String> goals = lines.stream().filter(next -> goalTime(next) == gameTime.get()).collect(Collectors.toList());

            if (goals.isEmpty()) {
                vertx.eventBus().publish("goals", "empty");
            } else {
                goals.forEach(c -> {
                    // there are sometimes more goals so wait 10 sec between each goal
                    int now = fastMode ? 1000 : 10 * 1000;
                    vertx.setTimer(now, t -> vertx.eventBus().publish("goals", c));
                });
            }
        });
    }

    private static Integer goalTime(String line) {
        return Integer.valueOf(line.split(",")[1]);
    }

}
