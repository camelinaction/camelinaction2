package camelinaction;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;

public class OrderStatusBean {

    private ProducerTemplate template;

    public OrderStatusBean() {
    }
    
    public OrderStatusBean(CamelContext context) {
        setTemplate(context.createProducerTemplate());  
    }

    public void setTemplate(ProducerTemplate template) {
        this.template = template;
    }
    
    public String orderStatus(String orderId) {
        return template.requestBody("http://localhost:8080/order/status?id=" + orderId, null, String.class);                                                
    }


}  
