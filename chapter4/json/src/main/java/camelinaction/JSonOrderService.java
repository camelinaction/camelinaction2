package camelinaction;

import org.apache.camel.jsonpath.JsonPath;
import org.apache.camel.language.Bean;

/**
 * A bean that acts as a JSon order service to handle incoming JSon orders
 */
public class JSonOrderService {

    public String handleIncomingOrder(@JsonPath("$.order.customerId") int customerId,
                                      @JsonPath("$.order.item") String item,
                                      @Bean(ref = "guid", method = "generate") int orderId) {

        // convert the order to a CSV and inject the generated order id
        return String.format("%d,%d,%s", orderId, customerId, item);
    }

}
