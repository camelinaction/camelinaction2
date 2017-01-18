package camelinaction;

import java.io.File;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Demonstrates to use route scoped OnCompletion.
 */
public class FileRollbackOnCompletionTest extends CamelTestSupport {

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

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:confirm")
                    // use a route scoped onCompletion to be executed when the Exchange failed
                    .onCompletion().onFailureOnly()
                        // and call the onFailure method on this bean
                        .bean(FileRollback.class, "onFailure")
                    // must use end to denote the end of the onCompletion route
                    .end()
                    // here starts the regular route
                    .bean(OrderService.class, "createMail")
                    .log("Saving mail backup file")
                    .to("file:target/mail/backup")
                    .log("Trying to send mail to ${header.to}")
                    .bean(OrderService.class, "sendMail")
                    .log("Mail sent to ${header.to}");
            }
        };
    }
}
