package camelinaction;

import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;

/**
 * Component to simulate asynchronous communication with ERP system.
 */
public class ErpEndpoint extends DefaultEndpoint {

    public ErpEndpoint(String uri, Component component) {
        super(uri, component);
    }

    public boolean isSingleton() {
        return true;
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        throw new UnsupportedOperationException("ErpComponent does not support consumer");
    }

    public Producer createProducer() throws Exception {
        return new ErpProducer(this);
    }
}
