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
    public boolean isGold(@JsonPath("$.order.loyaltyCode") int customerId) {
        // its a gold customer if the loyalty code is < 1000
        return customerId < 1000;
    }

    /**
     * Is it a silver customer.
     * <p/>
     * Notice that we can use any kind of Camel bean parameter binding, so we can bind the message @Body, @Header and so on.
     */
    public boolean isSilver(@JsonPath("$.order.loyaltyCode") int customerId) {
        // its a silver customer if the loyalty code is between 1000 and 4999
        return customerId >= 1000 && customerId < 5000;
    }

}
