package camelinaction;

import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The Spring based example of {@link camelinaction.SplitterAggregateABCTest}.
 * See this class for more details.
 */
public class SpringSplitterAggregateABCTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/splitter-aggregate.xml");
    }

    @Test
    public void testSplitAggregateABC() throws Exception {
        MockEndpoint split = getMockEndpoint("mock:split");
        // we expect 3 messages to be split and translated into a quote
        split.expectedBodiesReceived("Camel rocks", "Hi mom", "Yes it works");

        MockEndpoint result = getMockEndpoint("mock:result");
        // and one combined aggregated message as output with all the quotes together
        result.expectedBodiesReceived("Camel rocks+Hi mom+Yes it works");

        template.sendBody("direct:start", "A,B,C");

        assertMockEndpointsSatisfied();
    }

}
