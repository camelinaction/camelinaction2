package camelinaction;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
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

    // TODO: add documentation
    // TODO: add readme file
    // TODO: add vertx-camel bridge to make Camel emit the goal events

    // to use fast mode where each 5 second is a minute
    private boolean fastMode = false;

    private final AtomicInteger gameTime = new AtomicInteger();

    private final AtomicBoolean clockRunning = new AtomicBoolean();

    @Override
    public void start() throws Exception {

        // create a vertx router to setup websocket and http server
        Router router = Router.router(vertx);

        // configure allowed inbound and outbound traffice
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

        initControls();

        streamLiveScore();
    }

    private void initControls() {
        vertx.eventBus().localConsumer("control", h -> {
            String action = (String) h.body();
            if ("start".equals(action)) {
                System.out.println("Starting clock");
                clockRunning.set(true);
                vertx.eventBus().publish("clock", "" + gameTime.get());
            } else if ("stop".equals(action)) {
                System.out.println("Stopping clock");
                clockRunning.set(false);
                vertx.eventBus().publish("clock", "Stopped");
            }
        });
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

        if (clockRunning.get()) {
            vertx.eventBus().publish("clock", "" + gameTime.get());
        } else {
            vertx.eventBus().publish("clock", "Stopped");
        }
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
            if (!clockRunning.get()) {
                // show clock as stopped
                vertx.eventBus().publish("clock", "Stopped");
                return;
            }

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
            if (!clockRunning.get()) {
                return;
            }

            int min = gameTime.get();
            if (min > 91) {
                return;
            }

            // stream all goals for the current game time
            List<String> goals = lines.stream().filter(next -> goalTime(next) == gameTime.get()).collect(Collectors.toList());

            if (goals.isEmpty()) {
                vertx.eventBus().publish("goals", "empty");
            } else {
                AtomicInteger delay = new AtomicInteger();
                int initial = fastMode ? 1 : 5 * 1000;
                delay.set(initial);
                goals.forEach(c -> {
                    // there are sometimes more goals so wait 5 sec between each goal
                    System.out.println("Publish goal in " + delay.get() + " msec for goal: " + c);
                    vertx.setTimer(delay.get(), t -> vertx.eventBus().publish("goals", c));
                    // delay between 8 - 12 sec for next goal
                    int extra = fastMode ? 500 : 8000 + new Random().nextInt(4000);
                    delay.set(delay.get() + extra);
                });
            }
        });

    }

    private static Integer goalTime(String line) {
        return Integer.valueOf(line.split(",")[1]);
    }

}
