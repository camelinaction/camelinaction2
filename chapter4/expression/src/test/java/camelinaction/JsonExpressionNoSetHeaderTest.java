package camelinaction;

import org.apache.camel.builder.RouteBuilder;

/**
 * Test to demonstrate using bean as expressions during routing.
 * <p/>
 * This variation does not use a header to store the region. Instead the bean is invoked directly from
 * recipient list EIP.
 */
public class JsonExpressionNoSetHeaderTest extends JsonExpressionTest {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("file://target/order")
                    // route the message according to the region, invoke the bean directly and call the region method
                    // and prefix the destination with "mock:queue:"
                    .recipientList(simple("mock:queue:${bean:camelinaction.CustomerService?method=region}"));
            }
        };
    }
}
