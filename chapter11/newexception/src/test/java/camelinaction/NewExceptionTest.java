package camelinaction;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class NewExceptionTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                onException(AuthorizationException.class)
                    .handled(true)
                    .process(new NotAllowedProcessor());

                onException(Exception.class)
                    .handled(true)
                    .process(new GeneralErrorProcessor());

                from("direct:start")
                    .log("User ${header.name} is calling us")
                    .filter(simple("${header.name} == 'Kaboom'"))
                        .throwException(new AuthorizationException("Forbidden"))
                    .end()
                    .to("mock:done");

            }
        };
    }

    @Test
    public void testNoError() throws Exception {
        getMockEndpoint("mock:done").expectedMessageCount(1);

        template.sendBodyAndHeader("direct:start", "Hello Camel", "name", "Camel");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testNewException() throws Exception {
        getMockEndpoint("mock:done").expectedMessageCount(0);

        try {
            template.sendBodyAndHeader("direct:start", "Hello Bomb", "name", "Kaboom");
            fail("Should have thrown exception");
        } catch (CamelExecutionException e) {
            // we expect a NullPointerException because that is what NotAllowedProcessor throws
            // while handling the first AuthorizationException which is thrown from the filter in the route
            assertIsInstanceOf(NullPointerException.class, e.getCause());
        }

        assertMockEndpointsSatisfied();
    }
}
