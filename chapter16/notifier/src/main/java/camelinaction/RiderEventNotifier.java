package camelinaction;

import java.util.Date;
import java.util.EventObject;

import org.apache.camel.management.event.ExchangeFailedEvent;
import org.apache.camel.support.EventNotifierSupport;

/**
 * A custom {@link org.apache.camel.spi.EventNotifier} which is used by
 * Rider Auto Parts to publish failed exchanges to a centralized log server in a custom way.
 */
public class RiderEventNotifier extends EventNotifierSupport {

    private RiderFailurePublisher publisher;
    private String appId;

    public RiderEventNotifier(String appId) {
        this.appId = appId;
    }

    public boolean isEnabled(EventObject eventObject) {
        // can be used for fine grained to determine whether to notify this event or not

        // we only want to notify in case of failures
        return eventObject instanceof ExchangeFailedEvent;
    }

    public void notify(EventObject eventObject) throws Exception {
        if (eventObject instanceof ExchangeFailedEvent) {
            ExchangeFailedEvent event = (ExchangeFailedEvent) eventObject;
            String id = event.getExchange().getExchangeId();
            Exception cause = event.getExchange().getException();
            Date now = new Date();

            publisher.publish(appId, id, now, cause.getMessage());
        }
    }

    public void setPublisher(RiderFailurePublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    protected void doStart() throws Exception {
        // here you can initialize services etc
    }

    @Override
    protected void doStop() throws Exception {
        // here you can cleanup services
    }
}
