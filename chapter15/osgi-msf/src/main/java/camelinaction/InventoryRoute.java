package camelinaction;

import org.apache.camel.builder.RouteBuilder;

/**
 * Java DSL routes for the inventory examples showing how to use startupOrder.
 */
public class InventoryRoute extends RouteBuilder {

    public InventoryRoute() {
    }    
    
    @Override
    public void configure() throws Exception {
         // this is the webservice route which is started last
        from("cxf:bean:inventoryEndpoint")
            .routeId("webservice").startupOrder(2)
            .to("direct:update")
            .transform().method("inventoryService", "replyOk");

        // this is the shared route which then must be started first
        from("direct:update")
            .routeId("update").startupOrder(1)
            .to("bean:inventoryService?method=updateInventory");
    }
}
