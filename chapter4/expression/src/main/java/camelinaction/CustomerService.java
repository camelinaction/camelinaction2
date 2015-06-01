package camelinaction;

import org.apache.camel.jsonpath.JsonPath;

/**
 * A bean that acts as a customer service
 */
public class CustomerService {

    /**
     * From what region is the customer?
     * <p/>
     * Notice that we can use any kind of Camel bean parameter binding, so we can bind the message @Body, @Header and so on.
     */
    public String region(@JsonPath("$.order.customerId") int customerId) {
        if (customerId < 1000) {
            return "US";
        } else if (customerId < 2000) {
            return "EMEA";
        } else {
            return "OTHER";
        }
    }

}
