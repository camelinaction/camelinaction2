package camelinaction;

import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.api.management.ManagedAttribute;
import org.apache.camel.api.management.ManagedResource;
import org.apache.camel.impl.DefaultEndpoint;

/**
 * A custom endpoint which is using Spring JMX to easily let it be managed from JMX.
 * <p/>
 * By using @ManagedResource it will be turned into a MBean which can be managed from JMX.
 * By using @ManagedAttribute we can expose the attributes we want to be managed.
 * There is also a @ManagedOperation you can use for operations.
 */
@ManagedResource(description = "Managed ERPEndpoint")
public class ERPEndpoint extends DefaultEndpoint {

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

    @ManagedAttribute
    public boolean isVerbose() {
        return verbose;
    }

    @ManagedAttribute
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

}
