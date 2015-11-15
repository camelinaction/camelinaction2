package camelinaction;

import javax.sql.DataSource;

import org.apache.camel.Header;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Wire-tap service bean.
 * <p/>
 * This bean inserts a copy (wire-tap) of the order into a JDBC database using Spring {@link JdbcTemplate}.
 */
public class WireTapService {

    private int counter;
    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertWireTap(String order, @Header("JMSRedelivered") boolean redelivery) throws Exception {
        // using old-school JdbcTemplate to perform a SQL operation from Java code with spring-jdbc
        JdbcTemplate jdbc = new JdbcTemplate(dataSource);
        String orderId = "" + ++counter;
        String orderValue = order;
        String orderRedelivery = "" + redelivery;

        jdbc.execute(String.format("insert into booktap (order_id, order_book, order_redelivery) values ('%s', '%s', '%s')",
                orderId, orderValue, orderRedelivery));
    }

}
