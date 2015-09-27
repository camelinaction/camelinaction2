package camelinaction;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.camel.api.management.mbean.ManagedRouteMBean;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class OldestTest extends CamelTestSupport {

    private int size = 20;
    private ScheduledExecutorService scheduler;

    @Override
    protected boolean useJmx() {
        return true;
    }

    @Test
    public void testOldestReporter() throws Exception {
        getMockEndpoint("mock:done").expectedMessageCount(size);

        ManagedRouteMBean route = context.getManagedRoute("myRoute", ManagedRouteMBean.class);
        OldestDurationReporter reporter = new OldestDurationReporter(route);

        // schedule a background task that logs the current throttle count
        scheduler = context.getExecutorServiceManager().newSingleThreadScheduledExecutor(this, "OldestReporter");
        scheduler.scheduleAtFixedRate(reporter, 1, 1, TimeUnit.SECONDS);

        // send some orders
        for (int i = 0; i < size; i++) {
            template.sendBody("seda:foo", "Message " + i);
        }

        assertMockEndpointsSatisfied(1, TimeUnit.MINUTES);

        // just a little delay before running
        Thread.sleep(5000);

        resetMocks();
        getMockEndpoint("mock:done").expectedMessageCount(size);
        log.info("Running again a 2nd time");

        // send some orders
        for (int i = 0; i < size; i++) {
            template.sendBody("seda:foo", "2nd-Message " + i);
        }

        assertMockEndpointsSatisfied(1, TimeUnit.MINUTES);
        // shutdown thread pool
        context.getExecutorServiceManager().shutdown(scheduler);
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("seda:foo").id("myRoute")
                    .log("Incoming ${body}")
                    // do some random delay so the oldest is different value
                    .delay(simple("${random(0,5000)}")).asyncDelayed()
                    .log("Done ${body}")
                    .to("mock:done");
            }
        };
    }
}
