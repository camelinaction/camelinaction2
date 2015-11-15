package camelinaction;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Order service bean.
 * <p/>
 * This bean inserts the order into a JDBC database using Spring {@link JdbcTemplate}.
 */
public class OrderService {

    private int counter;
    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertOrder(String order) throws Exception {
        if (order.contains("Donkey")) {
            throw new IllegalArgumentException("Donkeys is not allowed");
        }

        // using old-school JdbcTemplate to perform a SQL operation from Java code with spring-jdbc
        JdbcTemplate jdbc = new JdbcTemplate(dataSource);
        String orderId = "" + ++counter;
        String orderValue = order;

        jdbc.execute(String.format("insert into bookorders (order_id, order_book) values ('%s', '%s')",
                orderId, orderValue));
    }

}
