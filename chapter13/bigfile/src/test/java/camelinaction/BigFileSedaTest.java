package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Processing a big with concurrency using SEDA.
 */
public class BigFileSedaTest extends CamelTestSupport {

    @Test
    public void testBigFile() throws Exception {
        // use 60 sec shutdown timeout
        context.getShutdownStrategy().setTimeout(60);

        long start = System.currentTimeMillis();

        // since we use seda the file route is complete before the update
        // as the pending message will stack up a the seda:update queue
        // hence we just let Camel try to shutdown itself and as it does
        // this graceful it will only shutdown when all the messages
        // on the seda queues has been processed
        Thread.sleep(1000);
        context.stop();

        long delta = System.currentTimeMillis() - start;
        System.out.println("Took " + delta / 1000 + " seconds");
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("file:target/inventory?noop=true")
                    .log("Starting to process big file: ${header.CamelFileName}")
                    .split(body().tokenize("\n")).streaming()
                        .bean(InventoryService.class, "csvToObject")
                        .to("seda:update")
                    .end()
                    .log("Done processing big file: ${header.CamelFileName}");

                from("seda:update?concurrentConsumers=20")
                    .bean(InventoryService.class, "updateInventory");
            }
        };
    }
}
