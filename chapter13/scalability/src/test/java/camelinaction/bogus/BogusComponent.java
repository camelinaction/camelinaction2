package camelinaction.bogus;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.UriEndpointComponent;

public class BogusComponent extends UriEndpointComponent {

    public BogusComponent() {
        super(BogusEndpoint.class);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        BogusEndpoint answer = new BogusEndpoint(uri, this);
        answer.setName(remaining);
        return answer;
    }
}
