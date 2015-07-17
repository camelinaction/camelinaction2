package camelinaction;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 * Component to simulate asynchronous communication with ERP system.
 */
public class ErpComponent extends DefaultComponent {

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        return new ErpEndpoint(uri, this);
    }

}
