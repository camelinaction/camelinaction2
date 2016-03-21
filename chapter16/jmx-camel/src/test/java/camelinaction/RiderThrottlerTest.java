package camelinaction;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class RiderThrottlerTest extends CamelTestSupport {

    private int size = 20;
    private ScheduledExecutorService scheduler;

    @Override
    protected boolean useJmx() {
        return true;
    }

    private class ThrottleTask implements Runnable {

        private final RiderThrottlerReporter reporter;

        private ThrottleTask(RiderThrottlerReporter reporter) {
            this.reporter = reporter;
        }

        @Override
        public void run() {
            long count = 0;
            try {
                count = reporter.reportThrottler();
            } catch (Exception e) {
                // ignore
            }
            log.info("There are currently {} throttled orders", count);
        }
    }

    @Test
    public void testThrottlerReporter() throws Exception {
        // use notifier to known when we have processed all the messages
        NotifyBuilder notify = new NotifyBuilder(context).whenDone(size).create();

        // create the reporter using injector so Camel can do dependency injection
        RiderThrottlerReporter reporter = context.getInjector().newInstance(RiderThrottlerReporter.class);

        // schedule a background task that logs the current throttle count
        scheduler = context.getExecutorServiceManager().newSingleThreadScheduledExecutor(this, "ThrottleReporter");
        scheduler.scheduleAtFixedRate(new ThrottleTask(reporter), 1, 1, TimeUnit.SECONDS);

        // send some orders
        for (int i = 0; i < size; i++) {
            template.sendBody("seda:orders", "Order " + size);
        }

        // wait for all messages to be done
        log.info("Waiting to process all the messages...");
        assertTrue("Should process all messages", notify.matches(1, TimeUnit.MINUTES));

        // shutdown thread pool
        context.getExecutorServiceManager().shutdown(scheduler);
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RiderThrottledRoute();
    }
}
