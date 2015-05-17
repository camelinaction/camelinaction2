package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.idempotent.MemoryIdempotentRepository;
import org.apache.camel.spi.IdempotentRepository;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Idempotent consumer test.
 * <p/>
 * This test is using the default in memory idempotent repository.
 */
public class IdempotentTest extends CamelTestSupport {

    // use the memory idempotent repository
    private IdempotentRepository repo = new MemoryIdempotentRepository();

    @Test
    @SuppressWarnings("unchecked")
    public void testIdempotent() throws Exception {
        // the repo should not yet contain these unique keys
        assertFalse(repo.contains("123"));
        assertFalse(repo.contains("456"));
        assertFalse(repo.contains("789"));

        // we expect 3 non duplicate order messages
        getMockEndpoint("mock:order").expectedMessageCount(3);
        getMockEndpoint("mock:order").assertNoDuplicates(header("orderId"));

        // but there is 5 incoming messages, where as 2 should be duplicate messages
        getMockEndpoint("mock:inbox").expectedMessageCount(5);

        template.sendBodyAndHeader("seda:inbox", "Motor", "orderId", "123");
        template.sendBodyAndHeader("seda:inbox", "Motor", "orderId", "123");
        template.sendBodyAndHeader("seda:inbox", "Tires", "orderId", "789");
        template.sendBodyAndHeader("seda:inbox", "Brake pad", "orderId", "456");
        template.sendBodyAndHeader("seda:inbox", "Tires", "orderId", "789");

        assertMockEndpointsSatisfied();

        // the repo should contain these unique keys
        assertTrue(repo.contains("123"));
        assertTrue(repo.contains("456"));
        assertTrue(repo.contains("789"));
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("seda:inbox")
                    .log("Incoming order ${header.orderId}")
                    .to("mock:inbox")
                    .idempotentConsumer(header("orderId"), repo)
                        .log("Processing order ${header.orderId}")
                        .to("mock:order")
                    .end();
            }
        };
    }
}
