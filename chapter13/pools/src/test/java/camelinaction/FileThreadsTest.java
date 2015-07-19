package camelinaction;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * How to concurrent process files using threads EIP
 */
public class FileThreadsTest extends CamelTestSupport {

    private int files = 100;
    private final Random random = new Random();

    @Test
    public void testFileThreads() throws Exception {
        log.info("Creating {} files...", files);

        // create many files
        for (int i = 0; i < files; i++) {
            template.sendBodyAndHeader("file:target/inbox", "Message " + i, Exchange.FILE_NAME, "file-" + i + ".txt");
        }

        log.info("Starting route");

        // keep track of completed files
        NotifyBuilder notify = new NotifyBuilder(context).whenCompleted(files).create();

        // start route
        context.startRoute("myRoute");

        // wait for all files to be processed
        assertTrue("Should complete all files", notify.matches(60, TimeUnit.SECONDS));
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("file:target/inbox?delete=true").routeId("myRoute").noAutoStartup()
                    .log("About to process ${file:name} picked up using thread #${threadName}")
                    // use 10 active threads and no task-queue so we only pickup new files when there
                    // is a available thread in the thread pool to process the file
                    .threads(10).maxQueueSize(0)
                        .log("Processing ${file:name} using thread #${threadName}")
                        .process(new Processor() {
                            @Override
                            public void process(Exchange exchange) throws Exception {
                                // do some random delay between 0 - 5 sec
                                int delay = random.nextInt(5000);
                                Thread.sleep(delay);
                            }
                        })
                        .log("Processing ${file:name} using thread #${threadName} is done")
                    .end()
                    .to("log:done?groupSize=10");
            }
        };
    }
}
