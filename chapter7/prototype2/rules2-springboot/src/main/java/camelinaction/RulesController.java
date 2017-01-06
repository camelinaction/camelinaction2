package camelinaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.Produce;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring REST controller to setup the REST service.
 * <p/>
 * Notice that this code is almost identical to the JAX-RS code from the WildFly Swarm example.
 * The only different is that this code is using Spring REST instead of JAX-RS
 */
@RestController
@RequestMapping("/api")
@ConfigurationProperties(prefix = "rules") // inject properties from application.properties using this prefix
public class RulesController {

    // inject Camel template to call the Camel route from java code
    @Produce(uri = "direct:inventory")
    private FluentProducerTemplate producer;

    @RequestMapping(value = "rules/{cartIds}", method = RequestMethod.GET, produces = "application/json")
    public List<ItemDto> rules(@PathVariable String cartIds) {
        List<ItemDto> answer = new ArrayList<>();

        // find all items in inventory (use Camel to call legacy system)
        ItemsDto inventory = producer.request(ItemsDto.class);

        // filter out what we already have in the shopping cart
        if (inventory != null) {
            for (ItemDto item : inventory.getItems()) {
                boolean duplicate = cartIds != null && cartIds.contains("" + item.getItemNo());
                if (!duplicate) {
                    answer.add(item);
                }
            }
        }

        // sort the list based on the ones we have the most of
        Collections.sort(answer, new ItemSorter());
        return answer;
    }

    /**
     * Used for sorting the items to recommend.
     */
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
