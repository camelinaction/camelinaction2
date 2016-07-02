package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class SplitterBeanTest extends CamelTestSupport {

    @Test
    public void testSplitBean() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:split");
        mock.expectedMessageCount(3);

        Customer customer = CustomerService.createCustomer();

        template.sendBody("direct:start", customer);

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                    // split the message using the CustomerService bean
                    // which can be done using the method call expression
                    .split().method(CustomerService.class, "splitDepartments")
                        .to("log:split")
                        .to("mock:split");
            }
        };
    }
}
