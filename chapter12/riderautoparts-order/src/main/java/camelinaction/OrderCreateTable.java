package camelinaction;

import javax.sql.DataSource;

import org.apache.camel.CamelContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class OrderCreateTable {

    public OrderCreateTable(CamelContext camelContext) {
        DataSource ds = camelContext.getRegistry().lookupByNameAndType("myDataSource", DataSource.class);
        JdbcTemplate jdbc = new JdbcTemplate(ds);

        try {
            jdbc.execute("drop table riders_order");
        } catch (Exception e) {
            // ignore as the table may not already exists
        }
        jdbc.execute("create table riders_order "
            + "( customer_id varchar(10), ref_no varchar(10), part_id varchar(10), amount varchar(10) )");
    }
    
}
