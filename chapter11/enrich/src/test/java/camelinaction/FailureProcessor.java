package camelinaction;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class FailureProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Exception e = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        String failure = "The message failed because " + e.getMessage();
        exchange.getIn().setHeader("FailureMessage", failure);
    }

}
