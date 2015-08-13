package camelinaction;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;

/**
 * A custom producer.
 */
public class ERPProducer extends DefaultProducer {

    public ERPProducer(Endpoint endpoint) {
        super(endpoint);
    }

    @Override
    public ERPEndpoint getEndpoint() {
        return (ERPEndpoint) super.getEndpoint();
    }

    public void process(Exchange exchange) throws Exception {
        String input = exchange.getIn().getBody(String.class);

        // if the verbose switch is turned on then log to System out
        if (getEndpoint().isVerbose()) {
            System.out.println("Calling ERP with: " + input);
        }

        // simulate calling ERP system and setting reply on the OUT body
        exchange.getOut().setBody("Simulated response from ERP");
        // support propagating headers (by copying headers from IN -> OUT)
        exchange.getOut().setHeaders(exchange.getIn().getHeaders());
    }

}
