package camelinaction;

import org.apache.camel.Exchange;

public class MockAuditService implements AuditService {

    @Override
    public void audit(Exchange exchange) {
        exchange.getContext().createProducerTemplate().send("mock:audit", exchange);
    }

}
