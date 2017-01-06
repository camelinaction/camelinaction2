package camelinaction;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RulesService {

    private static final Logger LOG = LoggerFactory.getLogger(RulesService.class);

    // use rest template to call external REST service
    private final RestTemplate restTemplate = new RestTemplate();

    @HystrixCommand(fallbackMethod = "standardItems")
    public ItemDto[] rules(String rulesUrl, String id, String cartIds) {
        LOG.info("Calling rules service {}", rulesUrl);
        ItemDto[] items;
        // if not existing items in the shopping cart then use item 999 as special
        if (cartIds == null || cartIds.isEmpty()) {
            cartIds = "999";
        }
        items = restTemplate.getForObject(rulesUrl, ItemDto[].class, id, cartIds);
        LOG.info("Inventory items {}", (Object[]) items);
        return items;
    }

    public ItemDto[] standardItems(String rulesUrl, String id, String cartIds) {
        // a special item to use as fallback
        ItemDto special = new ItemDto();
        special.setItemNo(999);
        special.setDescription("Special Rider Auto Part premium service");
        special.setName("special");
        special.setNumber(100);

        return new ItemDto[]{special};
    }

}
