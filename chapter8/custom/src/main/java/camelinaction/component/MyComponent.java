package camelinaction.component;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;

import org.apache.camel.impl.UriEndpointComponent;

/**
 * Represents the component that manages {@link MyEndpoint}.
 */
public class MyComponent extends UriEndpointComponent {
    
    public MyComponent() {
        super(MyEndpoint.class);
    }

    public MyComponent(CamelContext context) {
        super(context, MyEndpoint.class);
    }

    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        Endpoint endpoint = new MyEndpoint(uri, this);
        setProperties(endpoint, parameters);
        return endpoint;
    }
}
