package camelinaction;

import io.reactivex.Flowable;
import junit.framework.TestCase;
import org.apache.camel.CamelContext;
import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.component.reactive.streams.api.CamelReactiveStreams;
import org.apache.camel.component.reactive.streams.api.CamelReactiveStreamsService;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.Test;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CamelFirstTest extends TestCase {

    private static final Logger LOG = LoggerFactory.getLogger(NumbersTest.class);

    @Test
    public void testCamelFirst() throws Exception {
        LOG.info("Starting RX-Java2 Flowable Camel first");

        // create Camel
        CamelContext camel = new DefaultCamelContext();

        // create Reative Camel
        CamelReactiveStreamsService rsCamel = CamelReactiveStreams.get(camel);

        camel.start();
        rsCamel.start();

        // create a publisher from Camel seda:words endpoint
        Publisher<String> publisher = rsCamel.from("seda:words", String.class);

        Flowable.fromPublisher(publisher)
            // upper case the word
            .map(w -> w.toUpperCase())
            // log the big number
            .doOnNext(w -> LOG.info(w))
            .subscribe();

        // send some words to Camel
        FluentProducerTemplate template = camel.createFluentProducerTemplate();

        template.withBody("Camel").to("seda:words").send();
        template.withBody("rocks").to("seda:words").send();
        template.withBody("streams").to("seda:words").send();
        template.withBody("as").to("seda:words").send();
        template.withBody("well").to("seda:words").send();

        // sleep a bit for reactive subscriber to complete
        Thread.sleep(1000);

        camel.stop();
        rsCamel.stop();
    }

}
