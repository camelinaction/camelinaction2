package camelinaction;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * An example using asynchronous processing.
 * <p/>
 * Both Jetty and the ERP component supports async processing which means they offer better
 * scalability as threads are not blocked.
 * <p/>
 * Run the two test methods (sync vs. async) and compare the threads being used when logging the input and output.
 */
public class ScalabilityTest extends CamelTestSupport {

    @Test
    public void testSync() throws Exception {
        String reply = template.requestBody("jetty:http://localhost:8090/webshop/action/search", "bumper", String.class);
        assertEquals("Some other action here", reply);

        // when running this test, notice the threads being used to log the input/output
        // in the sync case its the same thread
    }

    @Test
    public void testAsync() throws Exception {
        // a customer requests the pricing for the buying 4 bumpers (id = 1719)
        String reply = template.requestBody("jetty:http://localhost:8090/webshop/action/pricing", "1234;4;1719;bumper", String.class);
        // the reply comes back with the price last = 516
        assertEquals("1234;4;1719;bumper;516", reply);

        // when running this test, notice the threads being used to log the input/output
        // in the async case its different threads
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = super.createCamelContext();

        // add the ERP component
        context.addComponent("erp", new ErpComponent());

        return context;
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("jetty:http://localhost:8090/webshop?matchOnUriPrefix=true")
                    // convert to string so its visible in the log
                    .convertBodyTo(String.class)
                    // log input
                    .to("log:input")
                    .choice()
                        .when(header(Exchange.HTTP_PATH).isEqualTo("/action/pricing"))
                            // communicate with ERP to calculate pricing
                            .to("erp:pricing")
                        .otherwise()
                            // other actions
                            .to("direct:other")
                    .end()
                    // log output
                    .to("log:output");

                // just return a fixed reply for other actions
                from("direct:other")
                    .transform(constant("Some other action here"));
            }
        };
    }

}
