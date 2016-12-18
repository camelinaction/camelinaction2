package camelinaction.goal;

import java.util.List;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;

/**
 * The producer can gather the list of football games
 */
public class GoalProducer extends DefaultProducer {

    private final List<String> games;

    public GoalProducer(Endpoint endpoint, List<String> games) {
        super(endpoint);
        this.games = games;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getIn().setHeader("action", "games");
        exchange.getIn().setBody(games);
    }
}
