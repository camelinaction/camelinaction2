package camelinaction;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * A JAX-RS Resource class where we define the RESTful web service, using the JAX-RS annotations.
 * <p/>
 * This implementation is pure JAX-RS and are not using Apache Camel.
 * Notice how each operation uses the {@link Response} type to build the response message.
 */
@Path("/orders/")
@Produces("application/xml")
@Api(value = "/orders", description = "Sample JAX-RS service with Swagger documentation")
public class RestOrderService {

    private OrderService orderService;

    /**
     * To inject a implementation of the {@link OrderService}
     */
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * The GET order by id operation
     */
    @GET
    @Path("/{id}")
    @ApiOperation(value = "get an order by id", response = Order.class)
    @ApiParam(name = "id")
    public Response getOrder(@PathParam("id") int orderId) {
        Order order = orderService.getOrder(orderId);
        if (order != null) {
            return Response.ok(order).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    /**
     * The PUT update order operation
     */
    @PUT
    public Response updateOrder(Order order) {
        orderService.updateOrder(order);
        return Response.ok().build();
    }

    /**
     * The POST create order operation
     */
    @POST
    public Response createOrder(Order order) {
        String id = orderService.createOrder(order);
        return Response.ok(id).build();
    }

    /**
     * The DELETE cancel order operation
     */
    @DELETE
    @Path("/{id}")
    public Response cancelOrder(@PathParam("id") int orderId) {
        orderService.cancelOrder(orderId);
        return Response.ok().build();
    }
}
