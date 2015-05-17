package camelinaction;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class GeneralErrorProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String name = (String) exchange.getIn().getHeader("name");
        if ("Kaboom".equals(name)) {
            throw new AuthorizationException("Forbidden");
        }
    }
}
