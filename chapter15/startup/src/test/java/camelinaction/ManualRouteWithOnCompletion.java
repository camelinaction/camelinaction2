package camelinaction;

import camelinaction.inventory.UpdateInventoryInput;
import org.apache.camel.builder.RouteBuilder;

/**
 * A maintenance route which must be started manually to force updating
 * the inventory when a file is dropped into a special folder.
 * <p/>
 * You should start the route using JConsole and stop it again after use.
 * <p/>
 * This example uses onCompletion for stopping the route
 */
public class ManualRouteWithOnCompletion extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // when the exchange is completed then stop the route by
        // running this onCompletion
        onCompletion().process(new StopRouteProcessor("manual"));

        // ensure we only pickup one file at any given time
        from("file://target/inventory/manual?maxMessagesPerPoll=1")
            // use noAutoStartup to indicate this route should
            // NOT be started when Camel starts
            .routeId("manual").noAutoStartup()
            .log("Doing manual update with file ${file:name}")
            .split(body().tokenize("\n"))
                .convertBodyTo(UpdateInventoryInput.class)
                .to("direct:update")
            .end();
            // use end() to denote the end of the splitter sub-route
    }

}
