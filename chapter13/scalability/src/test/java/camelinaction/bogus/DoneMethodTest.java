package camelinaction.bogus;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * A test that demonstrates how to in {@link camelinaction.bogus.BogusProducer} to structure the code
 * to deal with exceptions before asynchronous task has been submitted. And how that affects which
 * boolean parameter to use on the {@link org.apache.camel.AsyncCallback} instance.
 */
public class DoneMethodTest extends CamelTestSupport {

    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = super.createCamelContext();
        context.addComponent("bogus", new BogusComponent());
        return context;
    }

    @Test
    public void testDoneTrue() throws Exception {
        try {
            template.requestBody("direct:start", "Donkey in Action", String.class);
            fail("Should throw exception");
        } catch (CamelExecutionException e) {
            assertIsInstanceOf(IllegalArgumentException.class, e.getCause());
            assertEquals("Real developers ride Camels", e.getCause().getMessage());
        }
    }

    @Test
    public void testDoneFalse() throws Exception {
        String out = template.requestBody("direct:start", "Camel in Action", String.class);
        assertEquals("Camel in Action;516", out);
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                    .log("Calling bogus with ${threadName}")
                    .to("bogus:foo").id("to-bogus")
                    .log("Response from bogus ${body} from ${threadName}");
            }
        };
    }
}
