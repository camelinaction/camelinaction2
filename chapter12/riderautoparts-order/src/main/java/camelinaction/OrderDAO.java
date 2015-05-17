package camelinaction;

import javax.sql.DataSource;

import camelinaction.order.InputOrder;
import org.apache.camel.spi.Registry;
import org.springframework.jdbc.core.JdbcTemplate;

public class OrderDAO {

    public void insertOrder(InputOrder order, Registry registry) {
        DataSource ds = registry.lookupByNameAndType("myDataSource", DataSource.class);
        JdbcTemplate jdbc = new JdbcTemplate(ds);

        Object[] args = new Object[] { order.getCustomerId(), order.getRefNo(), order.getPartId(), order.getAmount()};
        jdbc.update("insert into riders_order (customer_id, ref_no, part_id, amount) values (?, ?, ?, ?)", args);
    }

}
