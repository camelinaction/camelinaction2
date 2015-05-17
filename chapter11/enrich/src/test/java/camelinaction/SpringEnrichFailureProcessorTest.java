package camelinaction;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringEnrichFailureProcessorTest extends CamelSpringTestSupport {

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("camelinaction/SpringEnrichFailureProcessorTest.xml");
    }

    @Test
    public void testEnrichFailure() throws Exception {
        // we expect the message to end up in the dead letter queue
        // is the original incoming message, and not the transformed message that happens during routing
        getMockEndpoint("mock:dead").expectedBodiesReceived("Hello World");
        // the failure processor will enrich the message and set a header with a constructed error message
        getMockEndpoint("mock:dead").expectedHeaderReceived("FailureMessage", "The message failed because Forced");

        template.sendBody("direct:start", "Hello World");

        assertMockEndpointsSatisfied();
    }
}
