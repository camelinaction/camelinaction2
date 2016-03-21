package camelinaction;

import javax.sql.DataSource;

import camelinaction.order.InputOrder;
import camelinaction.order.OutputOrder;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class OrderTest extends CamelSpringTestSupport {

    private JdbcTemplate jdbc;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        // setup JDBC template
        DataSource ds = context.getRegistry().lookupByNameAndType("myDataSource", DataSource.class);
        jdbc = new JdbcTemplate(ds);

        // notice that the table is automatic created using the OrderCreateTable class.
    }

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/camel-route.xml");
    }

    @Test
    public void testOrderOk() throws Exception {
        // there should be 0 row in the database when we start
        int rows = jdbc.queryForObject("select count(*) from riders_order", Integer.class);
        assertEquals(0, rows);

        InputOrder input = new InputOrder();
        input.setCustomerId("4444");
        input.setRefNo("57123");
        input.setPartId("333");
        input.setAmount("50");

        // give CXF time to wake up
        Thread.sleep(2000);

        OutputOrder reply = template.requestBody("cxf:bean:orderEndpoint", input, OutputOrder.class);
        assertEquals("OK", reply.getCode());

        // there should be 1 row in the database with the inserted order
        rows = jdbc.queryForObject("select count(*) from riders_order", Integer.class);
        assertEquals(1, rows);
    }

    @Test
    public void testOrderFailOnce() throws Exception {
        // there should be 0 row in the database when we start
        int rows = jdbc.queryForObject("select count(*) from riders_order", Integer.class);
        assertEquals(0, rows);

        InputOrder input = new InputOrder();
        input.setCustomerId("4444");
        // by using FAIL-ONCE as ref no we simulate failure in first processing
        input.setRefNo("FAIL-ONCE");
        input.setPartId("333");
        input.setAmount("50");

        // give CXF time to wake up
        Thread.sleep(2000);

        OutputOrder reply = template.requestBody("cxf:bean:orderEndpoint", input, OutputOrder.class);
        assertEquals("OK", reply.getCode());

        // there should be 1 row in the database with the inserted order
        rows = jdbc.queryForObject("select count(*) from riders_order", Integer.class);
        assertEquals(1, rows);
    }

    @Test
    public void testOrderFailAll() throws Exception {
        // there should be 0 row in the database when we start
        int rows = jdbc.queryForObject("select count(*) from riders_order", Integer.class);
        assertEquals(0, rows);

        InputOrder input = new InputOrder();
        input.setCustomerId("4444");
        // by using FATAL as ref no we simulate failure in all processing
        input.setRefNo("FATAL");
        input.setPartId("333");
        input.setAmount("50");

        // give CXF time to wake up
        Thread.sleep(2000);

        OutputOrder reply = template.requestBody("cxf:bean:orderEndpoint", input, OutputOrder.class);
        assertEquals("ERROR: Simulated fatal error", reply.getCode());

        // there should still be 0 row in the database as the entire route was rolled back
        rows = jdbc.queryForObject("select count(*) from riders_order", Integer.class);
        assertEquals(0, rows);
    }

}
