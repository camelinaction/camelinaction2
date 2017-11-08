package camelinaction;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.bridge.BridgeEventType;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

/**
 * Vert.x verticle for live scores.
 */
public class LiveScoreVerticle extends AbstractVerticle {

    // to use fast mode where each 5 second is a minute
    private boolean fastMode = true;

    private final AtomicInteger gameTime = new AtomicInteger();
    private final AtomicBoolean clockRunning = new AtomicBoolean();

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

        // init control buttons
        initControls();

        // start streaming live score
        streamLiveScore();
    }

    private void initControls() {
        vertx.eventBus().<String>consumer("control", h -> {
            String action = h.body();
            if ("start".equals(action)) {
                System.out.println("Starting clock");
                clockRunning.set(true);
                vertx.eventBus().publish("clock", gameTime.get() + ":00");
            } else if ("stop".equals(action)) {
                System.out.println("Stopping clock");
                clockRunning.set(false);
                vertx.eventBus().publish("clock", "Stopped");
            }
        });
    }

    private void initGames() {
        try {
            // load list of games from file
            InputStream is = LiveScoreVerticle.class.getClassLoader().getResourceAsStream("games.csv");
            String text = IOHelper.loadText(is);
            Stream<String> games = Arrays.stream(text.split("\n"));

            // split each line and publish to vertx eventbus
            games.forEach(game -> vertx.eventBus().publish("games", game));
        } catch (Exception e) {
            System.out.println("Error reading games.csv file due " + e.getMessage());
        }

        // publish clock time also
        if (clockRunning.get()) {
            vertx.eventBus().publish("clock", gameTime.get() + ":00");
        } else {
            vertx.eventBus().publish("clock", "Stopped");
        }
    }

    private void streamLiveScore() {
        List<String> lines;

        try {
            // read the goal scores from file
            InputStream is = LiveScoreVerticle.class.getClassLoader().getResourceAsStream("goals.csv");
            String text = IOHelper.loadText(is);

            Stream<String> goals = Arrays.stream(text.split("\n"));

            // sort goals scored on minutes
            goals = goals.sorted((a, b) -> goalTime(a).compareTo(goalTime(b)));

            // store goals in a list
            lines = goals.collect(Collectors.toList());
        } catch (Exception e) {
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
            if (min > 92) {
                return;
            }

            System.out.println("Game time " + min + ":00");

            // publish game time
            vertx.eventBus().publish("clock", min + ":00");
        });

        System.out.println("Publishing live score");
        vertx.setPeriodic(time, event -> {
            if (!clockRunning.get()) {
                return;
            }

            int min = gameTime.get();
            if (min > 92) {
                return;
            }

            // stream all goals for the current game time
            List<String> goals = lines.stream().filter(next -> goalTime(next) == gameTime.get()).collect(Collectors.toList());

            if (goals.isEmpty()) {
                vertx.eventBus().publish("goals", "empty");
            } else {
                // to remember delay between each goal
                AtomicInteger delay = new AtomicInteger();
                int initial = fastMode ? 1 : 5 * 1000;
                delay.set(initial);

                goals.forEach(c -> {
                    // for each goal then publish them to the goals eventbus
                    // but simulate some delay between each goal so they are not all published at the same time

                    // there are sometimes more goals so wait 5 sec between each goal
                    System.out.println("Publish goal in " + delay.get() + " msec for goal: " + c);
                    vertx.setTimer(delay.get(), t -> vertx.eventBus().publish("goals", c));
                    // delay between 8 - 12 sec for next goal
                    int extra = fastMode ? 2000 : 8000 + new Random().nextInt(4000);
                    delay.set(delay.get() + extra);
                });
            }
        });

    }

    private static Integer goalTime(String line) {
        return Integer.valueOf(line.split(",")[1]);
    }

}
