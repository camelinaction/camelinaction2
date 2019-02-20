package camelinaction;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.spi.annotations.Component;
import org.apache.camel.support.DefaultComponent;

/**
 * Component to simulate communication with ERP system which we want to manage from JMX.
 */
@Component("erp")
public class ERPComponent extends DefaultComponent {

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        ERPEndpoint erp = new ERPEndpoint(uri, this);
        erp.setName(remaining);
        return erp;
    }

}

