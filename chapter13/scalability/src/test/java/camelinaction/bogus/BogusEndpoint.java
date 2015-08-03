package camelinaction.bogus;

import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriPath;

@UriEndpoint(scheme = "bogus", title = "Bogus", syntax = "bogus:name", producerOnly = true, label = "custom")
public class BogusEndpoint extends DefaultEndpoint {

    @UriPath
    private String name;

    public BogusEndpoint(String endpointUri, Component component) {
        super(endpointUri, component);
    }

    @Override
    public Producer createProducer() throws Exception {
        return new BogusProducer(this);
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        throw new UnsupportedOperationException("Consumer not supported");
    }

    @Override
    public boolean isSingleton() {
        return true;
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
