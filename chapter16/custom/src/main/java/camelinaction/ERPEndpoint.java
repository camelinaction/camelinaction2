package camelinaction;

import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.api.management.ManagedAttribute;
import org.apache.camel.api.management.ManagedOperation;
import org.apache.camel.api.management.ManagedResource;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;

/**
 *  Endpoint to simulate communication with ERP system which we want to manage from JMX.
 */
@UriEndpoint(scheme = "erp", title = "ERP", syntax = "erp:name", producerOnly = true, label = "legacy")
@ManagedResource(description = "Managed ERPEndpoint")
public class ERPEndpoint extends DefaultEndpoint {

    @UriPath
    private String name;
    @UriParam
    private boolean verbose;

    public ERPEndpoint(String endpointUri, Component component) {
        super(endpointUri, component);
    }

    public Producer createProducer() throws Exception {
        return new ERPProducer(this);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        throw new UnsupportedOperationException("Consumer not supported");
    }

    public boolean isSingleton() {
        return true;
    }

    @ManagedAttribute(description = "Verbose logging enabled")
    public boolean isVerbose() {
        return verbose;
    }

    /**
     * Verbose logging enabled
     */
    @ManagedAttribute(description = "Verbose logging enabled")
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    @ManagedAttribute(description = "Logical name of endpoint")
    public String getName() {
        return name;
    }

    /**
     * Logical name of endpoint.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Operation to perform a PING test of the ERP system.
     */
    @ManagedOperation(description = "Ping test of the ERP system")
    public String ping() {
        return "PONG";
    }

}
