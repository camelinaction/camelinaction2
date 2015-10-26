package camelinaction;

import java.net.ConnectException;
import java.util.concurrent.TimeUnit;
import javax.sql.DataSource;

import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class RiderAutoPartsPartnerClientAcknowledgeModeTest extends CamelSpringTestSupport {

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
    public boolean isUseAdviceWith() {
        // we use advice-with in one of the tests
        return true;
    }

    @Test
    public void testSendPartnerReportIntoDatabase() throws Exception {
        NotifyBuilder notify = new NotifyBuilder(context).whenDone(1).create();

        // start Camel manually as we use advice-with in this unit tests class
        context.start();

        // there should be 0 row in the database when we start
        assertEquals(0, jdbc.queryForInt("select count(*) from partner_metric"));

        String xml = "<?xml version=\"1.0\"?><partner id=\"123\"><date>201503180816</date><code>200</code><time>4387</time></partner>";
        template.sendBody("activemq:queue:partners", xml);

        // wait for the route to complete one message
        assertTrue(notify.matches(10, TimeUnit.SECONDS));

        // there should be 1 row in the database
        assertEquals(1, jdbc.queryForInt("select count(*) from partner_metric"));
    }

    @Test
    public void testNoConnectionToDatabase() throws Exception {
        NotifyBuilder notify = new NotifyBuilder(context).whenDone(1).create();

        RouteBuilder rb = new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                interceptSendToEndpoint("sql:*")
                    .skipSendToOriginalEndpoint()
                    .throwException(new ConnectException("Cannot connect to the database"));
            }
        };

        // adviseWith enhances our route by adding the interceptor from the route builder
        // this allows us here directly in the unit test to add interceptors so we can simulate the connection failure
        context.getRouteDefinition("partnerToDB").adviceWith(context, rb);

        // start Camel after advice
        context.start();

        // there should be 0 row in the database when we start
        assertEquals(0, jdbc.queryForInt("select count(*) from partner_metric"));

        String xml = "<?xml version=\"1.0\"?><partner id=\"123\"><date>201503180816</date><code>200</code><time>4387</time></partner>";
        template.sendBody("activemq:queue:partners", xml);

        // wait for the route to complete one message
        assertTrue(notify.matches(10, TimeUnit.SECONDS));

        // data not inserted so there should be 0 rows
        assertEquals(0, jdbc.queryForInt("select count(*) from partner_metric"));
    }

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("camelinaction/RiderAutoPartsPartnerClientAcknowledgeModeTest.xml");
    }

}
