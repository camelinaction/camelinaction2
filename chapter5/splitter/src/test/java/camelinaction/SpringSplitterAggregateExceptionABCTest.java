package camelinaction;

import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The Splitter using its build in Aggregator example.
 * <p/>
 * This example will split a message into 3 message each containing the letters A, B and C.
 * Each of those message is then translated into a quote using the {@link camelinaction.WordTranslateBean} bean.
 * The Splitter will then aggregate those messages into a single combined outgoing message.
 * This is done using the {@link MyIgnoreFailureAggregationStrategy}.
 */
public class SpringSplitterAggregateExceptionABCTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/splitter-aggregate-exception.xml");
    }

    @Test
    public void testSplitAggregateExceptionABC() throws Exception {
        MockEndpoint split = getMockEndpoint("mock:split");
        // we expect 2 messages successfully to be split and translated into a quote
        split.expectedBodiesReceived("Camel rocks", "Yes it works");

        MockEndpoint result = getMockEndpoint("mock:result");
        // and one combined aggregated message as output with two two quotes together
        result.expectedBodiesReceived("Camel rocks+Yes it works");

        // F is an unknown word so it will cause an exception in the WordTranslateBean
        // but the MyIgnoreFailureAggregationStrategy will just ignore the exception
        // and continue routing
        template.sendBody("direct:start", "A,F,C");

        assertMockEndpointsSatisfied();
    }
}
