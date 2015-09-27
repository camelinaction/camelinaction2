package camelinaction;

import org.apache.camel.api.management.mbean.ManagedRouteMBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A reported to report which of the current inflight exchanges has been taking the longest time (the oldest).
 */
public class OldestDurationReporter implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(OldestDurationReporter.class);

    private final ManagedRouteMBean route;

    public OldestDurationReporter(ManagedRouteMBean route) {
        this.route = route;
    }

    @Override
    public void run() {
        Long duration = route.getOldestInflightDuration();
        if (duration != null) {
            LOG.info("Oldest inflight duration {} ms.", duration);
        } else {
            LOG.info("No inflight messages");
        }
    }

}
