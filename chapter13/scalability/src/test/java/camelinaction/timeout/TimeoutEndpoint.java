package camelinaction.timeout;

import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;

@UriEndpoint(scheme = "timeout", title = "Timeout", syntax = "timeout:name", producerOnly = true, label = "custom")
public class TimeoutEndpoint extends DefaultEndpoint {

    @UriPath
    private String name;

    @UriParam(defaultValue = "10000")
    private int timeout = 10000;

    public TimeoutEndpoint(String endpointUri, Component component) {
        super(endpointUri, component);
    }

    @Override
    public Producer createProducer() throws Exception {
        return new TimeoutProducer(this, timeout);
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

    public int getTimeout() {
        return timeout;
    }

    /**
     * Timeout in millis
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
