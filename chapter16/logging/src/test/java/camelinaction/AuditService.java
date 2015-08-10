package camelinaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple audit service which will AUDIT log incoming orders
 */
public class AuditService {

    private Logger LOG = LoggerFactory.getLogger(AuditService.class);

    public void auditFile(String body) {
        // transform the message into pieces we can grab interesting data from
        String[] parts = body.split(",");
        String id = parts[0];
        String customerId = parts[1];

        // construct the Audit message according to requirements
        // which is often something readable by humans
        String msg = "Customer " + customerId + " send order id " + id;

        // audit log it
        LOG.info(msg);
    }

}
