package camelinaction;

import javax.sql.DataSource;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class SpringCommitTest extends CamelSpringTestSupport {

    private JdbcTemplate jdbc;

    @Before
    public void setupDatabase() throws Exception {
        DataSource ds = context.getRegistry().lookupByNameAndType("myDataSource", DataSource.class);
        jdbc = new JdbcTemplate(ds);

        jdbc.execute("create table partner_metric "
                + "( partner_id varchar(10), time_occurred varchar(20), status_code varchar(3), perf_time varchar(10) )");
        jdbc.execute("insert into partner_metric (partner_id, time_occurred, status_code, perf_time) values ('123', '20170315183457', '200', '1503')");
    }

    @After
    public void dropDatabase() throws Exception {
        if (jdbc != null) {
            jdbc.execute("drop table partner_metric");
        }
    }

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("SpringCommitTest.xml");
    }

    @Test
    public void testCommit() throws Exception {
        getMockEndpoint("mock:result").expectedMessageCount(1);

        // we should pickup one row and process it
        assertMockEndpointsSatisfied();

        // give the database time to commit and delete the row
        Thread.sleep(1000);

        // and there should be 0 rows in the database
        int rows = jdbc.queryForObject("select count(*) from partner_metric", Integer.class);
        assertEquals(0, rows);

        context.stop();
    }

}
