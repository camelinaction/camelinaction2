package camelinaction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Processing a big with concurrency using the parallelProcessing option.
 */
public class BigFileCachedThreadPoolTest extends CamelTestSupport {
	
	private ExecutorService threadPool;
	
	@Before
	@Override
	public void setUp() throws Exception {
		// must create the custom thread pool before Camel is starting
		threadPool = Executors.newCachedThreadPool();
		super.setUp();
	}

	@After
	@Override
	public void tearDown() throws Exception {
		// proper cleanup by shutting down the thread pool
		threadPool.shutdownNow();
		super.tearDown();
	}

    @Test
    public void testBigFile() throws Exception {
        // when the first exchange is done
        NotifyBuilder notify = new NotifyBuilder(context).whenDoneByIndex(0).create();

        long start = System.currentTimeMillis();

        System.out.println("Waiting to be done with 1 min timeout (use ctrl + c to stop)");
        notify.matches(60, TimeUnit.SECONDS);

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
                    // by enabling the parallel processing the output from the split EIP will
                    // be processed concurrently using the cached thread pool settings
                    // which creates new threads as needed 
                    .split(body().tokenize("\n")).streaming().executorService(threadPool)
                        .bean(InventoryService.class, "csvToObject")
                        .to("direct:update")
                    .end()
                    .log("Done processing big file: ${header.CamelFileName}");

                from("direct:update")
                    .bean(InventoryService.class, "updateInventory");
            }
        };
    }
}
