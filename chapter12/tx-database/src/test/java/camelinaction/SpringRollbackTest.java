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

public class SpringRollbackTest extends CamelSpringTestSupport {

    private JdbcTemplate jdbc;

    @Before
    public void setupDatabase() throws Exception {
        DataSource ds = context.getRegistry().lookupByNameAndType("myDataSource", DataSource.class);
        jdbc = new JdbcTemplate(ds);

        jdbc.execute("create table partner_metric "
                + "( partner_id varchar(10), time_occurred varchar(20), status_code varchar(3), perf_time varchar(10) )");
        jdbc.execute("insert into partner_metric (partner_id, time_occurred, status_code, perf_time) values ('123', '20170515183457', '200', '1503')");
    }

    @After
    public void dropDatabase() throws Exception {
        if (jdbc != null) {
            jdbc.execute("drop table partner_metric");
        }
    }

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("SpringRollbackTest.xml");
    }

    @Test
    public void testRollback() throws Exception {
        // we should keep failing so wait for 5 or more failures
        NotifyBuilder notify = new NotifyBuilder(context).whenFailed(5).create();

        // should take a bit before we process 5 or more, because we use backoff on error
        assertTrue(notify.matches(20, TimeUnit.SECONDS));

        // and there should be 1 rows in the database as we keep rolling back
        int rows = jdbc.queryForObject("select count(*) from partner_metric", Integer.class);
        assertEquals(1, rows);

        context.stop();
    }

}
