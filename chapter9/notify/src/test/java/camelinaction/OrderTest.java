package camelinaction;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.apache.camel.Exchange;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.spi.BrowsableEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Demonstrates how to do integration testing without mocks.
 * <p/>
 * This example uses a client {@link OrderClient} to send messages to a JMS queue.
 * Which a Camel application will route.
 * <p/>
 * By using integration testing we then have to test that the client and Camel application
 * worked as excepted. For that we need to use NotifyBuilder to have Camel help us by telling
 * when the message has been processed. Then thereafter we can inspect whether or not the
 * message was routed to the expected JMS destination, and the content is correct as well.
 */
public class OrderTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/camel-context.xml");
    }

    @Test
    public void testOrderClientValid() throws Exception {
        // notify when one message is done
        NotifyBuilder notify = new NotifyBuilder(context).whenDone(1).create();

        // setup order information
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.set(Calendar.YEAR, 2017);
        cal.set(Calendar.MONTH, Calendar.APRIL);
        cal.set(Calendar.DAY_OF_MONTH, 20);
        cal.set(Calendar.HOUR_OF_DAY, 7);
        cal.set(Calendar.MINUTE, 47);
        cal.set(Calendar.SECOND, 58);
        Date date = cal.getTime();

        // send an order using the client
        OrderClient client = new OrderClient("tcp://localhost:61616");
        client.sendOrder(123, date, "4444", "5555");

        // use the notifier to wait for Camel to process the message
        // wait at most 5 seconds to avoid blocking forever if something goes wrong
        boolean matches = notify.matches(5, TimeUnit.SECONDS);
        // true means the notifier condition matched (= 1 message is done)
        assertTrue(matches);

        // should be one message in confirm queue
        BrowsableEndpoint be = context.getEndpoint("activemq:queue:confirm", BrowsableEndpoint.class);
        List<Exchange> list = be.getExchanges();
        assertEquals(1, list.size());
        assertEquals("OK,123,2017-04-20T07:47:58,4444,5555", list.get(0).getIn().getBody(String.class));
    }

    @Test
    public void testOrderClientInvalid() throws Exception {
        NotifyBuilder notify = new NotifyBuilder(context).whenDone(1).create();

        Calendar cal = Calendar.getInstance(Locale.US);
        cal.set(Calendar.YEAR, 2017);
        cal.set(Calendar.MONTH, Calendar.APRIL);
        cal.set(Calendar.DAY_OF_MONTH, 20);
        cal.set(Calendar.HOUR_OF_DAY, 7);
        cal.set(Calendar.MINUTE, 47);
        cal.set(Calendar.SECOND, 58);
        Date date = cal.getTime();

        OrderClient client = new OrderClient("tcp://localhost:61616");
        // when using customer id 999 we force an invalid order
        client.sendOrder(999, date, "5555", "2222");

        boolean matches = notify.matches(5, TimeUnit.SECONDS);
        assertTrue(matches);

        // should be one message in confirm queue
        BrowsableEndpoint be = context.getEndpoint("activemq:queue:invalid", BrowsableEndpoint.class);
        List<Exchange> list = be.getExchanges();
        assertEquals(1, list.size());
        assertEquals("999,2017-04-20T07:47:58,5555,2222", list.get(0).getIn().getBody(String.class));
    }

    @Test
    public void testOrderClientFailure() throws Exception {
        // now we expect the message to fail so we use whenFailed
        NotifyBuilder notify = new NotifyBuilder(context).whenFailed(1).create();

        Calendar cal = Calendar.getInstance(Locale.US);
        cal.set(Calendar.YEAR, 2017);
        cal.set(Calendar.MONTH, Calendar.APRIL);
        cal.set(Calendar.DAY_OF_MONTH, 20);
        cal.set(Calendar.HOUR_OF_DAY, 7);
        cal.set(Calendar.MINUTE, 47);
        cal.set(Calendar.SECOND, 58);
        Date date = cal.getTime();

        OrderClient client = new OrderClient("tcp://localhost:61616");
        // by using 9999 as the last item id we force an exception to occur
        client.sendOrder(123, date, "4444", "9999");

        boolean matches = notify.matches(5, TimeUnit.SECONDS);
        assertTrue(matches);
    }

}
