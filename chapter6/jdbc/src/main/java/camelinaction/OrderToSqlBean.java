package camelinaction;

import java.util.Map;

import org.apache.camel.Headers;
import org.apache.camel.language.XPath;

public class OrderToSqlBean {

    public String toSql(@XPath("order/@name") String name,
                        @XPath("order/@amount") int amount,
                        @XPath("order/@customer") String customer,
                        @Headers Map<String, Object> outHeaders) {
        outHeaders.put("partName", name);
        outHeaders.put("quantity", amount);
        outHeaders.put("customer", customer);
        return "insert into incoming_orders (part_name, quantity, customer) values (:?partName, :?quantity, :?customer)";
    }
}
