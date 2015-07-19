package camelinaction;

import java.util.Random;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * A processor that random delay processing between 0 and 5 seconds
 */
public class DelayProcessor implements Processor {

    private final Random random = new Random();

    @Override
    public void process(Exchange exchange) throws Exception {
        // do some random delay between 0 - 5 sec
        int delay = random.nextInt(5000);
        Thread.sleep(delay);

    }
}
