package camelinaction;

import java.net.ConnectException;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class OnExceptionFallbackTest extends CamelTestSupport {

    @Override
    public boolean isUseRouteBuilder() {
        // each unit test include their own route builder
        return false;
    }

    /**
     * This tests shows no match for onException and therefore fallback on the error handler
     * and by which there are no explicit configured. Therefore default error handler will be
     * used which by default does NO redelivery attempts.
     */
    @Test
    public void testOnExceptionFallbackToErrorHandler() throws Exception {
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                context.setTracing(true);

                onException(IllegalArgumentException.class).maximumRedeliveries(3);

                from("direct:order")
                    .bean(OrderServiceBean.class, "handleOrder")
                    .bean(OrderServiceBean.class, "saveToDB");
            }
        });
        context.start();

        try {
            template.requestBody("direct:order", "Camel in Action");
            fail("Should throw an exception");
        } catch (CamelExecutionException e) {
            assertIsInstanceOf(OrderFailedException.class, e.getCause());
            assertIsInstanceOf(ConnectException.class, e.getCause().getCause());
        }
    }

}
