package camelinaction;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * A JAX-RS Resource interface where we define the RESTful web service, using the JAX-RS annotations.
 * <p/>
 * When using cxfrs with Camel then this resource class can be an interface, as its just a contract/facade
 * to describe the REST web service. When a client calls the REST services then its a Camel route that routes
 * the incoming request, see {@link OrderRoute}
 * <br/>
 * This REST service supports both xml and json as data format.
 */
@Path("/orders/")
@Consumes(value = "application/xml,application/json")
@Produces(value = "application/xml,application/json")
public interface RestOrderService {

    /**
     * The GET order by id operation
     */
    @GET
    @Path("/{id}")
    Order getOrder(@PathParam("id") int orderId);

    /**
     * The PUT update order operation
     */
    @PUT
    void updateOrder(Order order);

    /**
     * The POST create order operation
     */
    @POST
    String createOrder(Order order);

    /**
     * The DELETE cancel order operation
     */
    @DELETE
    @Path("/{id}")
    void cancelOrder(@PathParam("id") int orderId);
}
