package camelinaction;

import java.util.concurrent.TimeUnit;

import org.apache.camel.Exchange;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The sample example as {@link AggregateABCRecoverTest} but using Spring XML instead.
 * <p/>
 * Please see code comments in the other example.
 */
public class SpringAggregateABCRecoverTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/aggregate-abc-recover.xml");
    }

    @Override
    public void setUp() throws Exception {
        // ensure we use a fresh repo each time by deleting the data directory
        deleteDirectory("data");
        super.setUp();
    }

    @Test
    public void testABCRecover() throws Exception {
        // we should never get a result
        getMockEndpoint("mock:result").expectedMessageCount(0);

        MockEndpoint mock = getMockEndpoint("mock:aggregate");
        // we should try 1 time + 4 times as redelivery
        mock.expectedMessageCount(5);

        MockEndpoint dead = getMockEndpoint("mock:dead");
        // we should send the message to dead letter channel when exhausted
        dead.expectedBodiesReceived("ABC");
        // should be marked as redelivered
        dead.message(0).header(Exchange.REDELIVERED).isEqualTo(true);
        // and we did try 4 times to redeliver
        dead.message(0).header(Exchange.REDELIVERY_COUNTER).isEqualTo(4);

        // send the first message
        template.sendBodyAndHeader("direct:start", "A", "myId", 1);
        // send the 2nd message with the same correlation key
        template.sendBodyAndHeader("direct:start", "B", "myId", 1);
        // now we have 3 messages with the same correlation key
        // and the Aggregator should publish the message
        template.sendBodyAndHeader("direct:start", "C", "myId", 1);

        // wait for 20 seconds as this test takes some time
        assertMockEndpointsSatisfied(20, TimeUnit.SECONDS);
    }

}
