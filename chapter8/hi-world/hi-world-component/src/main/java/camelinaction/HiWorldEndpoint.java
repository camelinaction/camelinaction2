package camelinaction;

import java.util.Map;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriPath;
import org.apache.camel.util.component.AbstractApiEndpoint;
import org.apache.camel.util.component.ApiMethod;
import org.apache.camel.util.component.ApiMethodPropertiesHelper;

import camelinaction.api.HiWorldFileHello;
import camelinaction.api.HiWorldJavadocHello;
import camelinaction.internal.HiWorldApiCollection;
import camelinaction.internal.HiWorldApiName;
import camelinaction.internal.HiWorldConstants;
import camelinaction.internal.HiWorldPropertiesHelper;

/**
 * Represents a HiWorld endpoint.
 */
@UriEndpoint(scheme = "hiworld", title = "HiWorld", syntax="hiworld:name", consumerClass = HiWorldConsumer.class, label = "HiWorld")
public class HiWorldEndpoint extends AbstractApiEndpoint<HiWorldApiName, HiWorldConfiguration> {

    @UriPath @Metadata(required = "true")
    private String name;

    // TODO create and manage API proxy
    private Object apiProxy;

    public HiWorldEndpoint(String uri, HiWorldComponent component,
                         HiWorldApiName apiName, String methodName, HiWorldConfiguration endpointConfiguration) {
        super(uri, component, apiName, methodName, HiWorldApiCollection.getCollection().getHelper(apiName), endpointConfiguration);

    }

    public Producer createProducer() throws Exception {
        return new HiWorldProducer(this);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        // make sure inBody is not set for consumers
        if (inBody != null) {
            throw new IllegalArgumentException("Option inBody is not supported for consumer endpoint");
        }
        final HiWorldConsumer consumer = new HiWorldConsumer(this, processor);
        // also set consumer.* properties
        configureConsumer(consumer);
        return consumer;
    }

    @Override
    protected ApiMethodPropertiesHelper<HiWorldConfiguration> getPropertiesHelper() {
        return HiWorldPropertiesHelper.getHelper();
    }

    protected String getThreadProfileName() {
        return HiWorldConstants.THREAD_PROFILE_NAME;
    }

    @Override
    protected void afterConfigureProperties() {
        // TODO create API proxy, set connection properties, etc.
        switch (apiName) {
            case HELLO_FILE:
                apiProxy = new HiWorldFileHello();
                break;
            case HELLO_JAVADOC:
                apiProxy = new HiWorldJavadocHello();
                break;
            default:
                throw new IllegalArgumentException("Invalid API name " + apiName);
        }
    }

    @Override
    public Object getApiProxy(ApiMethod method, Map<String, Object> args) {
        return apiProxy;
    }

    /**
     * Some description of this option, and what it does
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}
