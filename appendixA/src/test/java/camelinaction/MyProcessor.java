package camelinaction;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.SimpleBuilder;

public class MyProcessor implements Processor {
    public void process(Exchange exchange) throws Exception {
        SimpleBuilder simple = new SimpleBuilder(
                                       "${body} contains 'Camel'");
        if (!simple.matches(exchange)) {
            throw new Exception("This is NOT a Camel message");
        }
    }
}
