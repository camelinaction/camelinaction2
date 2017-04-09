package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import camelinaction.inventory.UpdateInventoryInput;
import org.wildfly.extension.camel.CamelAware;

@CamelAware
public class InventoryRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // this is the file route which is started 2nd last
        from("file://target/inventory/updates")
            .routeId("file").startupOrder(2)
            .split(body().tokenize("\n"))
            .convertBodyTo(UpdateInventoryInput.class)
            .to("direct:update");

        // this is the shared route which then must be started first
        from("direct:update")
            .routeId("update").startupOrder(1)
            .to("bean:inventoryService?method=updateInventory");
    }
}
