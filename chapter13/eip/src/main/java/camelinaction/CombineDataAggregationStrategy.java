package camelinaction;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

public class CombineDataAggregationStrategy implements AggregationStrategy {

    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        // to contains the endpoint which we send to so we know which system
        // has returned data back to us
        String to = newExchange.getProperty(Exchange.TO_ENDPOINT, String.class);
        if (to.contains("erp")) {
            return aggregate("ERP", oldExchange, newExchange);
        } else if (to.contains("crm")) {
            return aggregate("CRM", oldExchange, newExchange);
        } else {
            return aggregate("SHIPPING", oldExchange, newExchange);
        }
    }

    public Exchange aggregate(String system, Exchange oldExchange, Exchange newExchange) {
        // the first time oldExchange is null so we got to look out for that
        Exchange answer = oldExchange == null ? newExchange : oldExchange;
        // store data temporary in headers so we can combine data later
        answer.getIn().setHeader(system, newExchange.getIn().getBody());
        return answer;
    }

}
