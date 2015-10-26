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

public class RiderAutoPartsPartnerTransactedTest extends CamelSpringTestSupport {

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

    @Test
    public void testSendPartnerReportIntoDatabase() throws Exception {
        NotifyBuilder notify = new NotifyBuilder(context).whenDone(1).create();

        // there should be 0 row in the database when we start
        assertEquals(Long.valueOf(0), jdbc.queryForObject("select count(*) from partner_metric", Long.class));

        String xml = "<?xml version=\"1.0\"?><partner id=\"123\"><date>201503180816</date><code>200</code><time>4387</time></partner>";
        template.sendBody("activemq:queue:partners", xml);

        // wait for the route to complete one message
        assertTrue(notify.matches(10, TimeUnit.SECONDS));

        // there should be 1 row in the database
        assertEquals(Long.valueOf(1), jdbc.queryForObject("select count(*) from partner_metric", Long.class));
    }

    @Test
    public void testNoConnectionToDatabase() throws Exception {
        // AMQ will out of the box try to redeliver the message up till 6 times, and then move the message to its DLQ
        // so wait for 1 + 6 messages
        NotifyBuilder notify = new NotifyBuilder(context).whenDone(1 + 6).create();

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

        // there should be 0 row in the database when we start
        assertEquals(Long.valueOf(0), jdbc.queryForObject("select count(*) from partner_metric", Long.class));

        String xml = "<?xml version=\"1.0\"?><partner id=\"123\"><date>201503180816</date><code>200</code><time>4387</time></partner>";
        template.sendBody("activemq:queue:partners", xml);

        // wait for the route to complete
        // AMQ will out of the box try to redeliver the message up till 6 times, and then move the message to its DLQ
        assertTrue(notify.matches(15, TimeUnit.SECONDS));

        // data not inserted so there should be 0 rows
        assertEquals(Long.valueOf(0), jdbc.queryForObject("select count(*) from partner_metric", Long.class));

        // now check that the message was moved to the DLQ
        Object body = consumer.receiveBody("activemq:queue:ActiveMQ.DLQ", 5000);
        assertNotNull("The message should have been moved to the ActiveMQ DLQ", body);
    }

    @Test
    public void testFailFirstTime() throws Exception {
        // we should fail first time, so there should be 1 + 1 redelivery
        NotifyBuilder notify = new NotifyBuilder(context).whenDone(1 + 1).create();

        RouteBuilder rb = new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                interceptSendToEndpoint("sql:*")
                    .to("log:intercepted?showAll=true")
                    .choice()
                        .when(header("JMSRedelivered").isEqualTo("false"))
                            .throwException(new ConnectException("Cannot connect to the database"))
                    .end();
            }
        };

        // adviseWith enhances our route by adding the interceptor from the route builder
        // this allows us here directly in the unit test to add interceptors so we can simulate the connection failure
        context.getRouteDefinition("partnerToDB").adviceWith(context, rb);

        // there should be 0 row in the database when we start
        assertEquals(Long.valueOf(0), jdbc.queryForObject("select count(*) from partner_metric", Long.class));

        String xml = "<?xml version=\"1.0\"?><partner id=\"123\"><date>201503180816</date><code>200</code><time>4387</time></partner>";
        template.sendBody("activemq:queue:partners", xml);

        // wait for the route to complete
        assertTrue(notify.matches(10, TimeUnit.SECONDS));

        // data is inserted so there should be 1 rows
        assertEquals(Long.valueOf(1), jdbc.queryForObject("select count(*) from partner_metric", Long.class));

        // now check that the message is not on the DLQ
        String dlq = consumer.receiveBody("activemq:queue:ActiveMQ.DLQ", 1000L, String.class);
        assertNull("Should not be in the ActiveMQ DLQ", dlq);
    }

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("camelinaction/RiderAutoPartsPartnerTXTest.xml");
    }

}
