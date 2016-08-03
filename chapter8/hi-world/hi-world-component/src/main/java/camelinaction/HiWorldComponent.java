package camelinaction;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.util.component.AbstractApiComponent;

import camelinaction.internal.HiWorldApiCollection;
import camelinaction.internal.HiWorldApiName;

/**
 * Represents the component that manages {@link HiWorldEndpoint}.
 */
public class HiWorldComponent extends AbstractApiComponent<HiWorldApiName, HiWorldConfiguration, HiWorldApiCollection> {

    public HiWorldComponent() {
        super(HiWorldEndpoint.class, HiWorldApiName.class, HiWorldApiCollection.getCollection());
    }

    public HiWorldComponent(CamelContext context) {
        super(context, HiWorldEndpoint.class, HiWorldApiName.class, HiWorldApiCollection.getCollection());
    }

    @Override
    protected HiWorldApiName getApiName(String apiNameStr) throws IllegalArgumentException {
        return HiWorldApiName.fromValue(apiNameStr);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String methodName, HiWorldApiName apiName,
                                      HiWorldConfiguration endpointConfiguration) {
        HiWorldEndpoint endpoint = new HiWorldEndpoint(uri, this, apiName, methodName, endpointConfiguration);
        endpoint.setName(methodName);
        return endpoint;
    }

    /**
     * To use the shared configuration
     */
    @Override
    public void setConfiguration(HiWorldConfiguration configuration) {
        super.setConfiguration(configuration);
    }

}
