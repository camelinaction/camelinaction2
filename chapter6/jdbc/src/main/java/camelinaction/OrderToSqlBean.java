package camelinaction;

import org.apache.camel.language.XPath;

public class OrderToSqlBean {

    public String toSql(@XPath("order/@name") String name,
                        @XPath("order/@amount") int amount,
                        @XPath("order/@customer") String customer) {

        StringBuilder sb = new StringBuilder();
        sb.append("insert into incoming_orders (part_name, quantity, customer) values (");
        sb.append("'").append(name).append("', ");
        sb.append("").append(amount).append(", ");
        sb.append("'").append(customer).append("') ");
        System.out.println(sb.toString());
        return sb.toString();
    }
}
