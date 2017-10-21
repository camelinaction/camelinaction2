package camelinaction;

import java.util.concurrent.TimeUnit;
import javax.sql.DataSource;

import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class SpringXARollbackBeforeActiveMQTest extends CamelSpringTestSupport {

    private JdbcTemplate jdbc;

    @Before
    public void setupDatabase() throws Exception {
        DataSource ds = context.getRegistry().lookupByNameAndType("myDataSource", DataSource.class);
        jdbc = new JdbcTemplate(ds);

        jdbc.execute("create table partner_metric "
                + "( partner_id varchar(10), time_occurred varchar(20), status_code varchar(3), perf_time varchar(10) )");
    }

    @After
    public void dropDatabase() throws Exception {
        if (jdbc != null) {
            jdbc.execute("drop table partner_metric");
        }
    }

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("SpringXARollbackBeforeActiveMQTest.xml");
    }

    @Test
    public void testRollbackBeforeActiveMQ() throws Exception {
        NotifyBuilder notify = new NotifyBuilder(context).whenReceived(10).create();

        jdbc.execute("insert into partner_metric (partner_id, time_occurred, status_code, perf_time) values ('123', '20170315183457', '200', '1503')");

        assertTrue(notify.matches(15, TimeUnit.SECONDS));

        // and there should be 1 row in the database as it keep rolling back
        int rows = jdbc.queryForObject("select count(*) from partner_metric", Integer.class);
        assertEquals(1, rows);

        String order = consumer.receiveBody("activemq:queue:order", 2000, String.class);
        assertNull("Should NOT be in order queue", order);

        context.stop();
    }

}
