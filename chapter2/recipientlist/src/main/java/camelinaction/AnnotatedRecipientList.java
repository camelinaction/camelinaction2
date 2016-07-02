package camelinaction;

import org.apache.camel.RecipientList;
import org.apache.camel.language.XPath;

/**
 * Recipient List bean that sends orders to the production and 
 * accounting queues if the order originated from a gold customer. 
 * Otherwise, the order is only sent to the accounting queue.
 * 
 * The recipient list annotation is used to accomplish this.
 */
public class AnnotatedRecipientList {

    @RecipientList
    public String[] route(@XPath("/order/@customer") String customer) {
        if (isGoldCustomer(customer)) {
            return new String[] {"jms:accounting", "jms:production"};
        } else {
            return new String[] {"jms:accounting"};
        }
    }

    private boolean isGoldCustomer(String customer) {
        return customer.equals("honda");
    }
}
