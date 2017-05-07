package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.leveldb.LevelDBAggregationRepository;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * The ABC example for using persistent Aggregator EIP.
 * <p/>
 * The test runs for 20 seconds where you can copy files to target/inbox folder.
 * This files are picked up and aggregated. Every 3th file triggers completion.
 * <p/>
 * You can let the test terminate by waiting until it stops. Then start it again
 * so you can continue. For example copy 1 files and wait for the test to stop.
 * Then start the test and copy 2 files, to see that it triggers a completion.
 */
public class AggregateABCLevelDBTest extends CamelTestSupport {

    @Test
    public void testABCLevelDB() throws Exception {
        System.out.println("Copy 3 files to target/inbox to trigger the completion");
        System.out.println("Files to copy:");
        System.out.println("  copy src/test/resources/a.txt target/inbox");
        System.out.println("  copy src/test/resources/b.txt target/inbox");
        System.out.println("  copy src/test/resources/c.txt target/inbox");
        System.out.println("\nSleeping for 20 seconds");
        System.out.println("You can let the test terminate (or press ctrl +c) and then start it again");
        System.out.println("Which should let you be able to resume.");

        Thread.sleep(20 * 1000);
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                LevelDBAggregationRepository levelDB = 
                    new LevelDBAggregationRepository("myrepo", "data/myrepo.dat");

                from("file:target/inbox")
                    // do a little logging when we load the file
                    .log("Consuming file ${file:name}")
                    .convertBodyTo(String.class)
                    // just aggregate all messages
                    .aggregate(constant(true), new MyAggregationStrategy())
                        // use LevelDB as the persistent repository
                        .aggregationRepository(levelDB)
                        // and complete when we got 3 messages
                        .completionSize(3)
                        // do a little logging for the published message
                        .log("Sending out ${body}")
                        // and send it to the mock
                        .to("mock:result");
            }
        };
    }
}
