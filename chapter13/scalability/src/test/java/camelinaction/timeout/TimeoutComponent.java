package camelinaction.timeout;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.UriEndpointComponent;

public class TimeoutComponent extends UriEndpointComponent {

    public TimeoutComponent() {
        super(TimeoutEndpoint.class);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        TimeoutEndpoint answer = new TimeoutEndpoint(uri, this);
        answer.setName(remaining);
        return answer;
    }
}
