package camelinaction;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import camelinaction.inventory.UpdateInventoryInput;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

/**
 * A maintenance route which must be started manually to force updating
 * the inventory when a file is dropped into a special folder.
 * <p/>
 * You should start the route using JConsole and stop it again after use.
 */
public class ManualRouteWithStop extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // ensure we only pickup one file at any given time
        from("file://target/inventory/manual?maxMessagesPerPoll=1")
            // use noAutoStartup to indicate this route should
            // NOT be started when Camel starts
            .routeId("manual").noAutoStartup()
            .log("Doing manual update with file ${file:name}")
            .split(body().tokenize("\n"))
                .convertBodyTo(UpdateInventoryInput.class)
                .to("direct:update")
            .end()
            // use end() to denote the end of the splitter sub-route
            .process(new Processor() {
                public void process(Exchange exchange) throws Exception {
                    // stop the route when we are done as we should only
                    // pickup one file at the time. And if you need to
                    // pickup more files then you have to start the route
                    // manually again.
                    exchange.getContext().getInflightRepository().remove(exchange, "manual");

                    // spawn a thread to stop the route
                    ExecutorService executor = getContext().getExecutorServiceManager().newSingleThreadExecutor(this, "StopRouteManually");
                    executor.submit(new Callable<Object>() {
                        @Override
                        public Object call() throws Exception {
                            log.info("Stopping route manually");
                            getContext().stopRoute("manual");
                            return null;
                        }
                    });
                }
            });
    }

}
