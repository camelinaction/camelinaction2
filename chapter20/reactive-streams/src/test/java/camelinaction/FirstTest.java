package camelinaction;

import junit.framework.TestCase;
import org.junit.Test;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

public class FirstTest extends TestCase {

    private static final Logger LOG = LoggerFactory.getLogger(NumbersTest.class);

    @Test
    public void testFirstVerbose() throws Exception {
        LOG.info("Starting Reactive-Core Flux first verbose mode");

        // create a publisher with just these words
        Publisher<String> publisher = Flux.just("Camel", "rocks", "streams", "as", "well");

        Flux<String> subscriber = Flux.from(publisher)
            // upper case the word
            .map(w -> w.toUpperCase())
            // log the big number
            .doOnNext(w -> LOG.info(w));

        // start the subscriber
        subscriber.subscribe();
    }

    @Test
    public void testFirst() throws Exception {
        LOG.info("Starting Reactive-Core Flux first");

        // use stream engine to subscribe from a timer interval that runs a continued
        // stream with data once per second
        Flux.just("Camel", "rocks", "streams", "as", "well")
            .map(String::toUpperCase)
            // log the big number
            .doOnNext(LOG::info)
            // start the subscriber so it runs
            .subscribe();
    }

}
