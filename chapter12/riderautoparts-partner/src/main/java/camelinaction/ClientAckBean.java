package camelinaction;

import javax.jms.JMSException;

import org.apache.camel.Exchange;
import org.apache.camel.component.jms.JmsMessage;
import org.apache.camel.support.SynchronizationAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Java bean that is used when using JMS Client Acknowledge Mode.
 * <p/>
 * In client mode you must explicit call the {@link javax.jms.Message#acknowledge()}  method to accept the consumed
 * message (= commit). In case of an exception being thrown or not calling the accept, when the JMS consumer is closed,
 * then the message, from the JMS broker point of view is not accepted, and the message will be redelivered on next attempt.
 * This works in the same way as JMS Transacted Acknowledge Mode does.
 */
public class ClientAckBean {

    private static final Logger LOG = LoggerFactory.getLogger(ClientAckBean.class);

    public void prepareForAcknowledge(Exchange exchange) throws JMSException {
        // grab the javax.jms.Message from the incoming Camel JmsMessage which has the getJmsMessage()
        // that returns the javax.jms.Message instance
        final javax.jms.Message jms = exchange.getIn(JmsMessage.class).getJmsMessage();

        // add UoW completion to call the JMS client acknowledge when the routing is complete (commit)
        // if the routing fails with an exception, then acknowledge is not called (rollback), as that
        exchange.addOnCompletion(new SynchronizationAdapter() {
            @Override
            public void onComplete(Exchange exchange) {
                LOG.info("Using JMS client acknowledge to accept the JMS message consumed: {}", jms);
                try {
                    jms.acknowledge();
                } catch (JMSException e) {
                    LOG.warn("JMS client acknowledge failed due: " + e.getMessage(), e);
                }
            }
        });
    }
}
