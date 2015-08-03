package camelinaction;

import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriPath;

/**
 * Component to simulate asynchronous communication with ERP system.
 */
@UriEndpoint(scheme = "erp", title = "ERP", syntax = "erp:name", producerOnly = true, label = "legacy")
public class ErpEndpoint extends DefaultEndpoint {

    @UriPath
    private String name;

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

    public String getName() {
        return name;
    }

    /**
     * Logical name of endpoint.
     */
    public void setName(String name) {
        this.name = name;
    }
}
