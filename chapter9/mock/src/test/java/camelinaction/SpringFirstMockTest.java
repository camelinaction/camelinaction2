package camelinaction;

import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Our first unit test using the Mock component
 */
public class SpringFirstMockTest extends CamelSpringTestSupport {

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("camelinaction/SpringFirstMockTest.xml");
    }

    @Test
    public void testQuote() throws Exception {
        // get the mock endpoint
        MockEndpoint quote = getMockEndpoint("mock:quote");
        // set expectations such as 1 message should arrive
        quote.expectedMessageCount(1);

        // fire in a message to Camel
        template.sendBody("stub:jms:topic:quote", "Camel rocks");

        // verify the result
        quote.assertIsSatisfied();
    }

}
