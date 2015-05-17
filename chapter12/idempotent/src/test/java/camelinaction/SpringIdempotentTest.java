package camelinaction;

import org.apache.camel.spi.IdempotentRepository;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Idempotent consumer test.
 * <p/>
 * This test is using the default in memory idempotent repository.
 */
public class SpringIdempotentTest extends CamelSpringTestSupport {

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("camelinaction/SpringIdempotentTest.xml");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testIdempotent() throws Exception {
        IdempotentRepository repo = context.getRegistry().lookupByNameAndType("repo", IdempotentRepository.class);

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

}
