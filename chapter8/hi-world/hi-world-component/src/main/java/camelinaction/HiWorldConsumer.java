package camelinaction;

import org.apache.camel.Processor;
import org.apache.camel.util.component.AbstractApiConsumer;

import camelinaction.internal.HiWorldApiName;

/**
 * The HiWorld consumer.
 */
public class HiWorldConsumer extends AbstractApiConsumer<HiWorldApiName, HiWorldConfiguration> {

    public HiWorldConsumer(HiWorldEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
    }

}
