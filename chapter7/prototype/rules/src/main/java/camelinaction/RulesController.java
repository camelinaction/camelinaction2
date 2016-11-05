package camelinaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.camel.ProducerTemplate;
import org.apache.camel.cdi.Uri;

@ApplicationScoped
@Path("/api")
public class RulesController {

    // inject Camel template to call the Camel route from java code
    @Inject
    @Uri("direct:start")
    private ProducerTemplate producer;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
    @Path("/rules/{cartIds}")
	public List<ItemDto> get(@PathParam("cartIds") String cartIds) {
        List<ItemDto> answer = new ArrayList<>();

        // find all items in inventory (use Camel to call legacy system)
        // TODO: when WF-Swarm is Camel 2.18 then use FluentProducerTemplate
        ItemsDto inventory = producer.requestBody((Object) "", ItemsDto.class);

        // filter out what we already have in the shopping cart
        for (ItemDto item : inventory.getItems()) {
            boolean duplicate = cartIds != null && cartIds.contains("" + item.getItemNo());
            if (!duplicate) {
                answer.add(item);
            }
        }

        // sort the list based on the ones we have the most of
        Collections.sort(answer, new ItemSorter());

        // return the answer
        return answer;
	}

	private static class ItemSorter implements Comparator<ItemDto> {

        @Override
        public int compare(ItemDto o1, ItemDto o2) {
            Integer n1 = o1.getNumber();
            Integer n2 = o2.getNumber();
            // we want the items with the highest number in our inventory first so -1
            return n1.compareTo(n2) * -1;
        }
    }
}