package camelinaction;

import org.apache.camel.jsonpath.JsonPath;

/**
 * A bean that acts as a customer service
 */
public class CustomerService {

    /**
     * Is it a gold customer.
     * <p/>
     * Notice that we can use any kind of Camel bean parameter binding, so we can bind the message @Body, @Header and so on.
     */
    public boolean isGoldCustomer(@JsonPath("$.order.customerId") int customerId) {
        // its a gold customer if the customer id is < 1000
        return customerId < 1000;
    }

    /**
     * Is it a silver customer.
     * <p/>
     * Notice that we can use any kind of Camel bean parameter binding, so we can bind the message @Body, @Header and so on.
     */
    public boolean isSilverCustomer(@JsonPath("$.order.customerId") int customerId) {
        // its a silver customer if the customer id is < 5000
        return customerId < 5000;
    }

}
