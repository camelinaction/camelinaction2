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

public class SpringXACommitTest extends CamelSpringTestSupport {

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
        return new ClassPathXmlApplicationContext("SpringXACommitTest.xml");
    }

    @Test
    public void testCommit() throws Exception {
        NotifyBuilder notify = new NotifyBuilder(context).whenDone(1).create();

        jdbc.execute("insert into partner_metric (partner_id, time_occurred, status_code, perf_time) values ('123', '20170315183457', '200', '1503')");

        assertTrue(notify.matches(10, TimeUnit.SECONDS));

        // give time for database
        Thread.sleep(1000);

        // and there should be 0 rows in the database
        int rows = jdbc.queryForObject("select count(*) from partner_metric", Integer.class);
        assertEquals(0, rows);

        String order = consumer.receiveBody("activemq:queue:order", 2000, String.class);
        assertNotNull("Should be in order queue", order);

        context.stop();
    }

}
