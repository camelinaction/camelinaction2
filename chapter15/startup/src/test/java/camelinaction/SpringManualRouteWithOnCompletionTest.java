package camelinaction;

import java.util.concurrent.TimeUnit;

import org.apache.camel.Exchange;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Testing how to stop a route when its done using on completion.
 * <p/>
 * The route is defined in Spring XML file, see the <tt>SpringManualRouteWithOnCompletionTest.xml</tt> file.
 */
public class SpringManualRouteWithOnCompletionTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/SpringManualRouteWithOnCompletionTest.xml");
    }

    @Override
    public void setUp() throws Exception {
        deleteDirectory("target/inventory");
        super.setUp();
    }

    @Test
    public void testManualRouteWithOnCompletion() throws Exception {
        // notify us when the exchange is done
        NotifyBuilder notify = new NotifyBuilder(context).whenDone(1).create();

        // we actually do not need to use a mock but since we wanted to show the trick
        // in the createRouteBuilders we do both. We could just have relied on using
        // the NotifyBuilder to signal when the exchange is done
        getMockEndpoint("mock:update").expectedMessageCount(2);

        // route should be stopped at startup
        assertTrue("Route should be stopped at startup", context.getRouteStatus("manual").isStopped());

        // then start the route
        context.startRoute("manual");

        // send a file which is picked up and processed
        String input = "4444,57123,Bumper,50\n4444,57124,Fender,87";
        template.sendBodyAndHeader("file:target/inventory/manual", input, Exchange.FILE_NAME, "manual.csv");

        // assert we got the message
        assertMockEndpointsSatisfied();

        // wait for the route to be done
        notify.matches(5, TimeUnit.SECONDS);

        // we gotta wait just a little extra to stop
        Thread.sleep(5000);

        // it should have stopped itself
        assertTrue("Route should be stopped", context.getRouteStatus("manual").isStopped());
    }

}
