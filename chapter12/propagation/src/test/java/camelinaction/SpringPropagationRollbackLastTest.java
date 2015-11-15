package camelinaction;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.sql.DataSource;

import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class SpringPropagationRollbackLastTest extends CamelSpringTestSupport {

    private JdbcTemplate jdbc;

    @Before
    public void setupDatabase() throws Exception {
        DataSource ds = context.getRegistry().lookupByNameAndType("myDataSource", DataSource.class);
        jdbc = new JdbcTemplate(ds);

        jdbc.execute("create table bookorders "
                + "( order_id varchar(10), order_book varchar(50) )");
        jdbc.execute("create table bookaudit "
                + "( order_id varchar(10), order_book varchar(50), order_redelivery varchar(5) )");
    }

    @After
    public void dropDatabase() throws Exception {
        jdbc.execute("drop table bookorders");
        jdbc.execute("drop table bookaudit");
    }

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        // we still use the spring xml file to setup the activemq, jta transaction and atomikos
        // as that is a lot of work to do in Java code only
        return new ClassPathXmlApplicationContext("SpringPropagationRollbackLastTest.xml");
    }

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @Test
    public void testAuditLogRollbackLast() throws Exception {
        // we should have 1 original message
        NotifyBuilder notify = new NotifyBuilder(context).whenDone(1).create();

        // simulate the audit-log will fail
        context.getRouteDefinition("audit").adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                // simulate error connecting to database
                interceptSendToEndpoint("bean:auditLogService").skipSendToOriginalEndpoint()
                        .throwException(new IOException("Cannot connect to database"));
            }
        });

        context.start();

        // there should be 0 row in the database when we start
        assertEquals(Long.valueOf(0), jdbc.queryForObject("select count(*) from bookorders", Long.class));
        assertEquals(Long.valueOf(0), jdbc.queryForObject("select count(*) from bookaudit", Long.class));

        template.sendBody("activemq:queue:inbox", "Camel in Action");

        // wait for the route to complete
        assertTrue(notify.matches(10, TimeUnit.SECONDS));

        // the database need a little sleep time before commits are visible
        Thread.sleep(1000);

        // and the message was sent to the order queue
        String reply = consumer.receiveBody("activemq:queue:order", 10000, String.class);
        assertEquals("Camel in Action", reply);

        // there should be 0 row in the database with the order, and 0 in the audit-log as it was rolled back only
        assertEquals(Long.valueOf(1), jdbc.queryForObject("select count(*) from bookorders", Long.class));
        assertEquals(Long.valueOf(0), jdbc.queryForObject("select count(*) from bookaudit", Long.class));

        // print the SQL
        log.info("The following orders was recorded in the orders ...");
        List<Map<String, Object>> rows = jdbc.queryForList("select * from bookorders");
        for (Map<String, Object> row : rows) {
            log.info("Book order[id={}, book={}]", row.get("order_id"), row.get("order_book"));
        }
        log.info("The following orders was recorded in the audit-log ...");
        rows = jdbc.queryForList("select * from bookaudit");
        for (Map<String, Object> row : rows) {
            log.info("Book audit-log[id={}, book={}, redelivery={}]", row.get("order_id"), row.get("order_book"), row.get("order_redelivery"));
        }
    }

}
