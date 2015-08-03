package camelinaction;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.UriEndpointComponent;

/**
 * Component to simulate asynchronous communication with ERP system.
 */
public class ErpComponent extends UriEndpointComponent{

    public ErpComponent() {
        super(ErpEndpoint.class);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        ErpEndpoint erp = new ErpEndpoint(uri, this);
        erp.setName(remaining);
        return erp;
    }

}
