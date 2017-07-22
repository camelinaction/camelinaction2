package camelinaction;

import io.reactivex.Flowable;
import junit.framework.TestCase;
import org.junit.Test;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FirstTest extends TestCase {

    private static final Logger LOG = LoggerFactory.getLogger(NumbersTest.class);

    @Test
    public void testFirstVerbose() throws Exception {
        LOG.info("Starting RX-Java2 Flowable first verbose mode");

        // create a publisher with just these words
        Publisher<String> publisher = Flowable.just("Camel", "rocks", "streams", "as", "well");

        Flowable<String> subscriber = Flowable.fromPublisher(publisher)
            // upper case the word
            .map(w -> w.toUpperCase())
            // log the big number
            .doOnNext(w -> LOG.info(w));

        // start the subscriber
        subscriber.subscribe();
    }

    @Test
    public void testFirst() throws Exception {
        LOG.info("Starting RX-Java2 Flowable first");

        // use stream engine to subscribe from a timer interval that runs a continued
        // stream with data once per second
        Flowable.just("Camel", "rocks", "streams", "as", "well")
            // upper case the word
            .map(String::toUpperCase)
            // log the big number
            .doOnNext(LOG::info)
            // start the subscriber so it runs
            .subscribe();
    }

}
