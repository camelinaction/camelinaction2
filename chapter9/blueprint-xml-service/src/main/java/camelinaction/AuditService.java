package camelinaction;

import org.apache.camel.Exchange;

/**
 * Audit service.
 */
public interface AuditService {

    /**
     * Audits the payload
     */
    void audit(Exchange exchange);
}
