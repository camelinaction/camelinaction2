package camelinaction;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Test to demonstrate using @JsonPath as bean predicates during routing
 */
public class JsonPredicateTest extends CamelTestSupport {

    @Override
    public void setUp() throws Exception {
        deleteDirectory("target/order");
        super.setUp();
    }

    @Test
    public void sendGoldOrder() throws Exception {
        getMockEndpoint("mock:queue:gold").expectedMessageCount(1);
        getMockEndpoint("mock:queue:silver").expectedMessageCount(0);
        getMockEndpoint("mock:queue:regular").expectedMessageCount(0);

        // prepare a JSon document from a String
        String json = "{ \"order\": { \"loyaltyCode\": 88, \"item\": \"ActiveMQ in Action\" } }";

        // store the order as a file which is picked up by the route
        template.sendBodyAndHeader("file://target/order", json, Exchange.FILE_NAME, "order.json");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void sendSilverOrder() throws Exception {
        getMockEndpoint("mock:queue:gold").expectedMessageCount(0);
        getMockEndpoint("mock:queue:silver").expectedMessageCount(1);
        getMockEndpoint("mock:queue:regular").expectedMessageCount(0);

        // prepare a JSon document from a String
        String json = "{ \"order\": { \"loyaltyCode\": 4444, \"item\": \"Camel in Action\" } }";

        // store the order as a file which is picked up by the route
        template.sendBodyAndHeader("file://target/order", json, Exchange.FILE_NAME, "order.json");

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("file://target/order")
                    .choice()
                        .when(method(CustomerService.class, "isGold")).to("mock:queue:gold")
                        .when(method(CustomerService.class, "isSilver")).to("mock:queue:silver")
                        .otherwise().to("mock:queue:regular");
            }
        };
    }
}
