package camelinaction;

import java.time.Duration;
import java.util.Random;

import junit.framework.TestCase;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

public class NumbersTest extends TestCase {

    private static final Logger LOG = LoggerFactory.getLogger(NumbersTest.class);

    @Test
    public void testNumbers() throws Exception {
        LOG.info("Starting Reactive-Core Flux numbers");

        // use stream engine to subscribe from a timer interval that runs a continued
        // stream with data once per second
        Flux.interval(Duration.ofSeconds(1))
            // map the message to a random number between 0..9
            .map(i -> new Random().nextInt(10))
            // filter out to only include big numbers, eg 6..9
            .filter(n -> n > 5)
            // log the big number
            .doOnNext(c -> LOG.info("Streaming big number {}", c))
            // start the subscriber so it runs
            .subscribe();

        // let it run for 10 seconds
        Thread.sleep(10000);
    }
}
