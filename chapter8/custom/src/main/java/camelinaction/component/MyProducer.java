package camelinaction.component;

import org.apache.camel.Exchange;
import org.apache.camel.support.DefaultProducer;

/**
 * The My producer.
 */
public class MyProducer extends DefaultProducer {

    public MyProducer(MyEndpoint endpoint) {
        super(endpoint);
    }

    public void process(Exchange exchange) throws Exception {
        System.out.println(exchange.getIn().getBody());    
    }

}
