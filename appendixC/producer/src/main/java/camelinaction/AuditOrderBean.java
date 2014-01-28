package camelinaction;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;

public class AuditOrderBean {

    private ProducerTemplate template;

    public AuditOrderBean(CamelContext context) {
        template = context.createProducerTemplate();                
    }

    public void auditOrder(String order) {
        template.sendBody("activemq:topic:order.audit", order);     
    }
}  
