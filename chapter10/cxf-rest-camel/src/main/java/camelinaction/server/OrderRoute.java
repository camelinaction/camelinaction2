package camelinaction.server;

import camelinaction.OrderService;
import org.apache.camel.builder.RouteBuilder;

/**
 * A Camel route that calls the {@link OrderService} backend.
 * <p/>
 * To keep this example simple the route is a straight from -> bean, but you can of course
 * make the routes do much more.
 */
public class OrderRoute extends RouteBuilder {

    private OrderService orderService;

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public void configure() throws Exception {
        from("direct:getOrder").routeId("getOrder")
            .bean(orderService, "getOrder");

        from("direct:updateOrder").routeId("updateOrder")
            .bean(orderService, "updateOrder");

        from("direct:createOrder").routeId("createOrder")
            .bean(orderService, "createOrder");

        from("direct:cancelOrder").routeId("cancelOrder")
            .bean(orderService, "cancelOrder");
    }
}
