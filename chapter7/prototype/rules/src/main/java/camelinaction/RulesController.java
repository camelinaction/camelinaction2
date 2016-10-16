package camelinaction;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/api")
public class RulesController {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
    @Path("/rules/{cartIds}")
	public List<ItemDto> get(@PathParam("cartIds") String cartIds) {
        System.out.println(cartIds);

        List<ItemDto> items = new ArrayList<>();
        items.add(new ItemDto(1, "foo", "blah", 85));
        items.add(new ItemDto(2, "bar", "blah blah", 82));
        return items;
	}
}