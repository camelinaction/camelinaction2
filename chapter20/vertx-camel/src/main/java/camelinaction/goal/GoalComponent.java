package camelinaction.goal;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 * A component to support live scoring streams
 */
public class GoalComponent extends DefaultComponent {

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        GoalEndpoint answer = new GoalEndpoint(uri, this, remaining);
        setProperties(answer, parameters);
        return answer;
    }
}
