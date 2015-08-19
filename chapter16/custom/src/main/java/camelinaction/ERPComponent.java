package camelinaction;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.impl.UriEndpointComponent;

/**
 * Component to simulate communication with ERP system which we want to manage from JMX.
 */
public class ERPComponent extends UriEndpointComponent {

    public ERPComponent() {
        super(ERPEndpoint.class);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        ERPEndpoint erp = new ERPEndpoint(uri, this);
        erp.setName(remaining);
        return erp;
    }

}

