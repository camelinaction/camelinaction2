package camelinaction;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

public class MyIgnoreFailureAggregationStrategy implements AggregationStrategy {

    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        // check if there was an exception thrown
        if (newExchange.getException() != null) {
            // yes there was, so we just handle it by ignoring it
            return oldExchange;
        }

        if (oldExchange == null) {
            // this is the first time so no existing aggregated exchange
            return newExchange;
        }

        // append the new word to the existing
        String body = newExchange.getIn().getBody(String.class);
        String existing = oldExchange.getIn().getBody(String.class);

        oldExchange.getIn().setBody(existing + "+" + body);
        return oldExchange;
    }
}
