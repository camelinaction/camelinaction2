package camelinaction;

import javax.ws.rs.Consumes;
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
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * A JAX-RS Resource class where we define the RESTful web service, using the JAX-RS annotations.
 * <p/>
 * This implementation is pure JAX-RS and are not using Apache Camel.
 * Notice how each operation uses the {@link Response} type to build the response message.
 */
@Path("/orders/")
@Consumes("application/json,application/xml")
@Produces("application/json,application/xml")
@Api(value = "/orders", description = "Rider Auto Parts Order Service")
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
    @ApiOperation(value = "Get order", response = Order.class)
    @ApiResponses({
        @ApiResponse(code = 200, response = Order.class, message = "The found order"),
        @ApiResponse(code = 404, response = String.class, message = "Cannot find order with the id")
    })
    public Response getOrder(
            @ApiParam(value = "The id of the order", required = true) @PathParam("id") int orderId) {
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
    @ApiOperation(value = "Update order")
    public Response updateOrder(
            @ApiParam(value = "The order to update", required = true) Order order) {
        orderService.updateOrder(order);
        return Response.ok().build();
    }

    /**
     * The POST create order operation
     */
    @POST
    @ApiOperation(value = "Create order")
    @ApiResponses({
        @ApiResponse(code = 200, response = String.class, message = "The id of the created order")
    })
    public Response createOrder(
            @ApiParam(value = "The order to create", required = true) Order order) {
        String id = orderService.createOrder(order);
        return Response.ok(id).build();
    }

    /**
     * The DELETE cancel order operation
     */
    @DELETE
    @Path("/{id}")
    public Response cancelOrder(
            @ApiParam(value = "The id of the order to cancel", required = true) @PathParam("id") int orderId) {
        orderService.cancelOrder(orderId);
        return Response.ok().build();
    }
}
