package camelinaction;

import io.reactivex.Flowable;
import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.reactive.streams.api.CamelReactiveStreams;
import org.apache.camel.component.reactive.streams.api.CamelReactiveStreamsService;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class CamelFilesTest extends CamelTestSupport {

    @Test
    public void testFiles() throws Exception {
        getMockEndpoint("mock:inbox").expectedMessageCount(4);
        getMockEndpoint("mock:camel").expectedMessageCount(2);

        CamelReactiveStreamsService rsCamel = CamelReactiveStreams.get(context);

        // use stream engine to subscribe from the publisher
        // where we filter out the big numbers which is logged
        Flowable.fromPublisher(rsCamel.from("file:target/inbox"))
            // call the direct:inbox Camel route from within this flow
            .doOnNext(e -> rsCamel.to("direct:inbox", e))
            // filter out files which has Camel in the text
            .filter(e -> e.getIn().getBody(String.class).contains("Camel"))
            // let Camel also be subscriber by the endpoint direct:camel
            .subscribe(rsCamel.subscriber("direct:camel"));

        // create some test files
        fluentTemplate.to("file:target/inbox").withBody("Hello World").withHeader(Exchange.FILE_NAME, "hello.txt").send();
        fluentTemplate.to("file:target/inbox").withBody("Hello Camel").withHeader(Exchange.FILE_NAME, "hello2.txt").send();
        fluentTemplate.to("file:target/inbox").withBody("Bye Camel").withHeader(Exchange.FILE_NAME, "bye.txt").send();
        fluentTemplate.to("file:target/inbox").withBody("Bye World").withHeader(Exchange.FILE_NAME, "bye2.txt").send();

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:inbox")
                    .log("Inbox ${header.CamelFileName}")
                    .wireTap("mock:inbox");

                from("direct:camel")
                    .log("This is a Camel file ${header.name}")
                    .to("mock:camel");
            }
        };
    }
}
