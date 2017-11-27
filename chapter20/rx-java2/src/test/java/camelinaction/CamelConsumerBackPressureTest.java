package camelinaction;

import io.reactivex.Flowable;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.reactive.streams.api.CamelReactiveStreams;
import org.apache.camel.component.reactive.streams.api.CamelReactiveStreamsService;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Test to demonstrate consumer side of back pressure
 */
public class CamelConsumerBackPressureTest extends CamelTestSupport {

    @Test
    public void testConsumerBackPressure() throws Exception {
        CamelReactiveStreamsService rsCamel = CamelReactiveStreams.get(context);

        // create an array with the messages
        String[] inbox = new String[100];
        for (int i = 0; i < 100; i++) {
            inbox[i] = "Hello " + i;
        }

        // use stream engine create a publisher
        Flowable.fromArray(inbox)
            .doOnRequest(n -> {
                // log each time we are request more data from the publisher
                log.info("Requesting {} messages", n);
            })
            .subscribe(rsCamel.streamSubscriber("inbox", String.class));

        // let it run for 10 seconds
        Thread.sleep(10 * 1000L);
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // allow at most 5 inflight messages and use 5 concurrent consumers
                from("reactive-streams:inbox?maxInflightExchanges=5&concurrentConsumers=5")
                    // use a little delay so us humans can follow what happens
                    .delay(constant(10))
                    .log("Processing message ${body}");
            }
        };
    }
}
