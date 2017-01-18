package camelinaction;

import java.io.File;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Demonstrates how to use UnitOfWork and Synchronization to have custom logic
 * being executed when the Exchange is done.
 * <p/>
 * We use this to rollback when the Exchange fails. See the {@link camelinaction.FileRollback} class.
 *
 * @see camelinaction.FileRollback
 */
public class FileRollbackTest extends CamelTestSupport {

    @Override
    public void setUp() throws Exception {
        deleteDirectory("target/mail/backup");
        super.setUp();
    }

    @Test
    public void testOk() throws Exception {
        // test without a failure
        template.sendBodyAndHeader("direct:confirm", "bumper", "to", "someone@somewhere.org");

        // which should cause a file to be written
        File file = new File("target/mail/backup/");
        String[] files = file.list();
        assertEquals("There should be one file", 1, files.length);
    }

    @Test
    public void testRollback() throws Exception {
        // simulate a failure
        try {
            template.sendBodyAndHeader("direct:confirm", "bumper", "to", "FATAL");
            fail("Should have thrown an exception");
        } catch (CamelExecutionException e) {
            assertIsInstanceOf(IllegalArgumentException.class, e.getCause());
            assertEquals("Simulated fatal error", e.getCause().getMessage());
        }

        // which should cause no files to be written in the backup folder
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
                    .process(new Processor() {
                        public void process(Exchange exchange) throws Exception {
                            // register the FileRollback as Synchronization
                            exchange.getUnitOfWork().addSynchronization(new FileRollback());

                            // this can be done a bit easier by using:
                            // exchange.addOnCompletion(new FileRollback());
                        }
                    })
                    // or use Java 8 style with lambda instead of the inlined processor above
                    // .process(e -> e.addOnCompletion(new FileRollback()))
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
