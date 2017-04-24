package camelinaction;

import java.io.File;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Demonstrates how to use OnCompletion in Spring XML.
 */
public class SpringFileRollbackOnCompletionTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/filerollback.xml");
    }

    @Override
    public void setUp() throws Exception {
        deleteDirectory("target/mail/backup");
        super.setUp();
    }

    @Test
    public void testOk() throws Exception {
        template.sendBodyAndHeader("direct:confirm", "bumper", "to", "someone@somewhere.org");

        File file = new File("target/mail/backup/");
        String[] files = file.list();
        assertEquals("There should be one file", 1, files.length);
    }

    @Test
    public void testRollback() throws Exception {
        try {
            template.sendBodyAndHeader("direct:confirm", "bumper", "to", "FATAL");
            fail("Should have thrown an exception");
        } catch (CamelExecutionException e) {
            assertIsInstanceOf(IllegalArgumentException.class, e.getCause());
            assertEquals("Simulated fatal error", e.getCause().getMessage());
        }

        File file = new File("target/mail/backup/");
        String[] files = file.list();
        assertEquals("There should be no files", 0, files.length);
    }
}
