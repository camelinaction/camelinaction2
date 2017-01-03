package camelinaction;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST service for the recommendation service.
 */
@EnableCircuitBreaker
@RestController
@RequestMapping("/api")
@ConfigurationProperties(prefix = "recommend") // inject properties from application.properties using this prefix
public class RecommendController {

    private static final Logger LOG = LoggerFactory.getLogger(RecommendController.class);

    // these are injected from the application.properties
    private String cartUrl;
    private String rulesUrl;
    private String ratingsUrl;

    @Autowired
    private ShoppingCartService shoppingCart;

    @Autowired
    private RulesService rules;

    @Autowired
    private RatingService rating;

    @RequestMapping(value = "recommend", method = RequestMethod.GET, produces = "application/json")
    public List<ItemDto> recommend(HttpSession session) {
        // make things easier and hardcode the session id to the same
        String id = "123";
        LOG.info("HTTP session id {}", id);

        // get the current item in the shopping cart associated with the HTTP session id
        String cartIds = shoppingCart.shoppingCart(cartUrl, id);

        // call rules service with the items from the shopping cart to get list of items to be recommended
        ItemDto[] items = rules.rules(rulesUrl, id, cartIds);
        String itemIds = itemsToCommaString(items);

        // call rating service
        RatingDto[] ratings = rating.rating(ratingsUrl, itemIds);
        // append ratings to items
        for (RatingDto rating : ratings) {
            appendRatingToItem(rating, items);
        }

        return Arrays.asList(items);
    }

    public String getCartUrl() {
        return cartUrl;
    }

    public void setCartUrl(String cartUrl) {
        this.cartUrl = cartUrl;
    }

    public String getRulesUrl() {
        return rulesUrl;
    }

    public void setRulesUrl(String rulesUrl) {
        this.rulesUrl = rulesUrl;
    }

    public String getRatingsUrl() {
        return ratingsUrl;
    }

    public void setRatingsUrl(String ratingsUrl) {
        this.ratingsUrl = ratingsUrl;
    }

    private void appendRatingToItem(RatingDto rating, ItemDto[] items) {
        for (ItemDto item : items) {
            if (item.getItemNo() == rating.getItemNo()) {
                item.setRating(rating.getRating());
            }
        }
    }

    private String itemsToCommaString(ItemDto[] items) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.length; i++) {
            ItemDto item = items[i];
            sb.append(item.getItemNo());
            if (i < items.length - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

}
