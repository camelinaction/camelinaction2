package camelinaction;

import javax.sql.DataSource;

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
        jdbc.execute("drop table partner_metric");
    }

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("SpringXACommitTest.xml");
    }

    @Test
    public void testXaCommit() throws Exception {
        // there should be 0 row in the database when we start
        assertEquals(Long.valueOf(0), jdbc.queryForObject("select count(*) from partner_metric", Long.class));

        String xml = "<?xml version=\"1.0\"?><partner id=\"123\"><date>201503180816</date><code>200</code><time>4387</time></partner>";
        template.sendBody("activemq:queue:partners", xml);

        // wait for the route to complete with success
        Thread.sleep(5000);

        // there should be 1 row in the database
        assertEquals(Long.valueOf(1), jdbc.queryForObject("select count(*) from partner_metric", Long.class));
    }

}
