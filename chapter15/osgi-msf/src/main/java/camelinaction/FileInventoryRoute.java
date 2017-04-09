package camelinaction;

import camelinaction.inventory.UpdateInventoryInput;
import org.apache.camel.builder.RouteBuilder;

/**
 * Java DSL routes for the inventory examples showing how to use startupOrder.
 */
public class FileInventoryRoute extends RouteBuilder {

    private String inputPath;
    private String routeId;
    
    @Override
    public void configure() throws Exception {
        from(inputPath)
            .routeId(getRouteId())
            .split(body().tokenize("\n"))
            .convertBodyTo(UpdateInventoryInput.class)
            .to("direct:update");
    }

    public String getInputPath() {
        return inputPath;
    }

    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }
}
