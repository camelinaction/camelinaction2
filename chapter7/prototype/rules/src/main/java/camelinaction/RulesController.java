package camelinaction;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/rules")
public class RulesController {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ItemDto> get() {
        List<ItemDto> items = new ArrayList<>();
        items.add(new ItemDto(1, "foo", "blah", 85));
        items.add(new ItemDto(2, "bar", "blah blah", 82));
        return items;
	}
}