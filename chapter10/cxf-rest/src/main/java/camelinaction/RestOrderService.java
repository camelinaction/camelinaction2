package camelinaction;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/orders/")
@Produces("text/xml")
public class RestOrderService {

    private OrderService orderService;

    public OrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @GET
    @Path("/{id}")
    public Response getOrder(@PathParam("id") int orderId) {
        Order order = orderService.getOrder(orderId);
        if (order != null) {
            return Response.ok(order).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    public Response updateOrder(Order order) {
        orderService.updateOrder(order);
        return Response.ok().build();
    }

    @POST
    public Response createOrder(Order order) {
        String id = orderService.createOrder(order);
        return Response.ok(id).build();
    }

    @DELETE
    @Path("/{id}")
    public Response cancelOrder(@PathParam("id") int orderId) {
        orderService.cancelOrder(orderId);
        return Response.ok().build();
    }
}
