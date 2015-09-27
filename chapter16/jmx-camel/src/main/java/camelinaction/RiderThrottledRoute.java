package camelinaction;

import org.apache.camel.builder.RouteBuilder;

/**
 * A order processing system to a legacy system that can only
 * allow processing 5 messages per 60 seconds.
 */
public class RiderThrottledRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("seda:orders").id("legacyRoute")
            .throttle(5).timePeriodMillis(10000).asyncDelayed().id("orderThrottler")
                .to("seda:legacy");
    }
}
