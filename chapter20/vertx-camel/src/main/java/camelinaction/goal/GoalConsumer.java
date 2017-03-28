package camelinaction.goal;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Suspendable;
import org.apache.camel.impl.DefaultConsumer;

import static java.lang.Thread.sleep;

/**
 * The consumer can stream a live score updates and support suspend/resume
 * to stop the game clock as well.
 */
public class GoalConsumer extends DefaultConsumer implements Suspendable {

    // to use fast mode where each 5 second is a minute
    private boolean fastMode = true;

    private final List<String> goals;
    private final AtomicInteger gameTime = new AtomicInteger(-1);
    private final Timer timer = new Timer();
    private TimerTask task;

    public GoalConsumer(Endpoint endpoint, Processor processor, List<String> goals) {
        super(endpoint, processor);
        this.goals = goals;
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();

        log.info("Starting goal live stream");
        if (task == null) {
            task = new GoalTask();
            int period = fastMode ? 5 * 1000 : 60 * 1000;
            timer.scheduleAtFixedRate(task, 1000, period);
        }
    }

    @Override
    public void doResume() throws Exception {
        log.info("Resuming goal live stream");

        // signal the clock is started
        Exchange exchange = getEndpoint().createExchange();
        exchange.getIn().setHeader("action", "clock");
        exchange.getIn().setBody(String.valueOf(gameTime.get()));
        getProcessor().process(exchange);
    }

    @Override
    protected void doSuspend() throws Exception {
        log.info("Suspending goal live stream");

        // signal the clock is stopped
        Exchange exchange = getEndpoint().createExchange();
        exchange.getIn().setHeader("action", "clock");
        exchange.getIn().setBody("Stopped");
        getProcessor().process(exchange);
    }

    @Override
    protected void doStop() throws Exception {
        log.info("Stopping goal live stream");
        timer.cancel();
        task = null;

        // signal the clock is stopped
        Exchange exchange = getEndpoint().createExchange();
        exchange.getIn().setHeader("action", "clock");
        exchange.getIn().setBody("Stopped");
        getProcessor().process(exchange);

        super.doStop();
    }

    /**
     * A {@link TimerTask} which runs every minute and routes messages to the vertx component
     * with updated game clock and goals.
     */
    private class GoalTask extends TimerTask {

        @Override
        public void run() {
            if (!isRunAllowed() || isSuspended()) {
                // the route has been suspended when the user click the stop button
                return;
            }

            // start from 0
            int min = gameTime.incrementAndGet();

            // if the match is ended then there is no more goals
            if (min > 92) {
                return;
            }

            try {
                // publish game time
                Exchange exchange = getEndpoint().createExchange();
                exchange.getIn().setHeader("action", "clock");
                exchange.getIn().setBody(min +":00");

                getProcessor().process(exchange);

                // stream all goals for the current game time
                List<String> newGoals = goals.stream().filter(next -> goalTime(next) == min).collect(Collectors.toList());

                if (newGoals.isEmpty()) {
                    exchange = getEndpoint().createExchange();
                    exchange.getIn().setHeader("action", "goal");
                    exchange.getIn().setBody("empty");
                    getProcessor().process(exchange);
                } else {
                    Iterator<String> it = newGoals.iterator();
                    while (it.hasNext()) {
                        exchange = getEndpoint().createExchange();
                        exchange.getIn().setHeader("action", "goal");
                        exchange.getIn().setBody(it.next());
                        getProcessor().process(exchange);
                        if (it.hasNext()) {
                            // there are more goals so wait a bit before next
                            // delay between 8 - 12 sec for next goal
                            int extra = fastMode ? 2000 : 8000 + new Random().nextInt(4000);
                            sleep(extra);
                        }
                    };
                }
            } catch (Exception e) {
                getExceptionHandler().handleException(e);
            }
        }
    }

    private static Integer goalTime(String line) {
        return Integer.valueOf(line.split(",")[1]);
    }

}
