package camelinaction;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.camel.ProducerTemplate;

/**
 * A JAX-RS Resource class where we define the RESTful web service, using the JAX-RS annotations.
 * <p/>
 * This implementation is pure JAX-RS and are not using Apache Camel.
 * Notice how each operation uses the {@link Response} type to build the response message.
 */
@Path("/orders/")
@Produces("application/json")
public class RestOrderService {

    private ProducerTemplate producer;

    /**
     * To inject a Camel {@link ProducerTemplate}
     */
    public void setProducerTemplate(ProducerTemplate producerTemplate) {
        this.producer = producerTemplate;
    }

    /**
     * The GET order by id operation
     */
    @GET
    @Path("/{id}")
    public Response getOrder(@PathParam("id") int orderId) {
        Order order = producer.requestBody("direct:getOrder", orderId, Order.class);
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
        producer.sendBody("direct:updateOrder", order);
        return Response.ok().build();
    }

    /**
     * The POST create order operation
     */
    @POST
    public Response createOrder(Order order) {
        String id = producer.requestBody("direct:createOrder", order, String.class);
        return Response.ok(id).build();
    }

    /**
     * The DELETE cancel order operation
     */
    @DELETE
    @Path("/{id}")
    public Response cancelOrder(@PathParam("id") int orderId) {
        producer.sendBody("direct:cancelOrder", orderId);
        return Response.ok().build();
    }
}
