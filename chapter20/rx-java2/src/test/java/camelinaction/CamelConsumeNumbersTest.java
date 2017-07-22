package camelinaction;

import io.reactivex.Flowable;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.reactive.streams.api.CamelReactiveStreams;
import org.apache.camel.component.reactive.streams.api.CamelReactiveStreamsService;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class CamelConsumeNumbersTest extends CamelTestSupport {

    @Test
    public void testConsumeNumbers() throws Exception {
        CamelReactiveStreamsService rsCamel = CamelReactiveStreams.get(context);

        // use stream engine create a publisher
        // that just sends 5 numbers, which needs to be sorted
        // and then each data is send to Camel on the reactive-streams:number endpoint
        Flowable.just("3", "4", "1", "5", "2")
            .sorted(String::compareToIgnoreCase)
            .subscribe(rsCamel.streamSubscriber("numbers", String.class));

        // let it run for 2 seconds
        Thread.sleep(2000);
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("reactive-streams:numbers")
                    .log("Got number ${body}");
            }
        };
    }
}
