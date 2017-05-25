package camelinaction;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Processor to stop a route by its name
 */
public class StopRouteProcessor implements Processor {

    private final static Logger LOG = LoggerFactory.getLogger(StopRouteProcessor.class);

    private final String name;

    /**
     * @param name route to stop
     */
    public StopRouteProcessor(String name) {
        this.name = name;
    }

    public void process(Exchange exchange) throws Exception {
        // force stopping this route while we are routing an Exchange
        // requires two steps:
        // 1) unregister from the inflight registry
        // 2) stop the route
        LOG.info("Stopping route: " + name);
        exchange.getContext().getInflightRepository().remove(exchange, name);
        exchange.getContext().stopRoute(name);
    }
}
