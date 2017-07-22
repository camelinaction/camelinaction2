package camelinaction;

import io.reactivex.Flowable;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.reactive.streams.api.CamelReactiveStreams;
import org.apache.camel.component.reactive.streams.api.CamelReactiveStreamsService;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.reactivestreams.Publisher;

public class CamelNumbersTest extends CamelTestSupport {

    @Test
    public void testNumbers() throws Exception {
        CamelReactiveStreamsService rsCamel = CamelReactiveStreams.get(context);

        // create a published that receive from the numbers stream
        Publisher<Integer> numbers = rsCamel.fromStream("numbers", Integer.class);

        // use stream engine to subscribe from the publisher
        // where we filter out the big numbers which is logged
        Flowable.fromPublisher(numbers)
            .filter(n -> n > 5)
            .doOnNext(c -> log.info("Streaming big number {}", c))
            .subscribe();

        // let it run for 10 seconds
        Thread.sleep(10000);
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // use timer to create a continued stream of numbers
                from("timer:number")
                    .transform(simple("${random(0,10)}"))
                    .log("Generated random number ${body}")
                    // send the numbers to the stream named numbers
                    .to("reactive-streams:numbers");
            }
        };
    }
}
