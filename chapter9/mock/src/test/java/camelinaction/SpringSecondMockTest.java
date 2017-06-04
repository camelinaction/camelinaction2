package camelinaction;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Our second unit test using the Mock component
 */
public class SpringSecondMockTest extends CamelSpringTestSupport {

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("camelinaction/SpringSecondMockTest.xml");
    }

    @Test
    public void testIsCamelMessage() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:quote");
        mock.expectedMessageCount(2);

        template.sendBody("stub:jms:topic:quote", "Hello Camel");
        template.sendBody("stub:jms:topic:quote", "Camel rocks");

        assertMockEndpointsSatisfied();

        List<Exchange> list = mock.getReceivedExchanges();
        String body1 = list.get(0).getIn().getBody(String.class);
        String body2 = list.get(1).getIn().getBody(String.class);
        assertTrue(body1.contains("Camel"));
        assertTrue(body2.contains("Camel"));
    }

    @Test
    public void testSameMessageArrived() throws Exception {
        // get the mock endpoint
        MockEndpoint quote = getMockEndpoint("mock:quote");
        // set expectations that the same message arrived as we send
        quote.expectedBodiesReceived("Camel rocks");

        // fire in a message to Camel
        template.sendBody("stub:jms:topic:quote", "Camel rocks");

        // verify the result
        quote.assertIsSatisfied();
    }

    @Test
    public void testTwoMessages() throws Exception {
        // get the mock endpoint
        MockEndpoint quote = getMockEndpoint("mock:quote");
        // set expectations the two messages arrives in any order
        quote.expectedBodiesReceivedInAnyOrder("Camel rocks", "Hello Camel");

        // fire in a messages to Camel
        template.sendBody("stub:jms:topic:quote", "Hello Camel");
        template.sendBody("stub:jms:topic:quote", "Camel rocks");

        // verify the result
        quote.assertIsSatisfied();
    }

    @Test
    public void testTwoMessagesOrdered() throws Exception {
        // get the mock endpoint
        MockEndpoint quote = getMockEndpoint("mock:quote");
        // set expectations the two messages arrives in specified order
        quote.expectedBodiesReceived("Hello Camel", "Camel rocks");

        // fire in a messages to Camel
        template.sendBody("stub:jms:topic:quote", "Hello Camel");
        template.sendBody("stub:jms:topic:quote", "Camel rocks");

        // verify the result
        quote.assertIsSatisfied();
    }

    @Test
    public void testContains() throws Exception {
        // get the mock endpoint
        MockEndpoint quote = getMockEndpoint("mock:quote");
        // set expectations the two messages arrives in specified order
        quote.expectedMessageCount(2);
        // all messages should contain the Camel word
        quote.allMessages().body().contains("Camel");

        // fire in a messages to Camel
        template.sendBody("stub:jms:topic:quote", "Hello Camel");
        template.sendBody("stub:jms:topic:quote", "Camel rocks");

        // verify the result
        quote.assertIsSatisfied();
    }
}
