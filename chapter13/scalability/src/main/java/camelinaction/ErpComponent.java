package camelinaction;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.spi.annotations.Component;
import org.apache.camel.support.DefaultComponent;

/**
 * Component to simulate asynchronous communication with ERP system.
 */
@Component("erp")
public class ErpComponent extends DefaultComponent {

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        ErpEndpoint erp = new ErpEndpoint(uri, this);
        erp.setName(remaining);
        return erp;
    }

}
