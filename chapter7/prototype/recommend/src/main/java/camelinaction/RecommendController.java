package camelinaction;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
@ConfigurationProperties(prefix = "recommend")
public class RecommendController {

    private final RestTemplate restTemplate = new RestTemplate();

    private String rulesUrl;

    @RequestMapping(value = "recommend", method = RequestMethod.GET, produces = "application/json")
    public List<ItemDto> recommend() {

        // here we can do logic to find out the session/user to pass on the rules engine
        // call the rules backend

        List<ItemDto> items = new ArrayList<>();
        items.add(new ItemDto(1, "foo", "blah", 85));
        items.add(new ItemDto(2, "bar", "blah blah", 82));

        return items;
    }

    public String getRulesUrl() {
        return rulesUrl;
    }

    public void setRulesUrl(String rulesUrl) {
        this.rulesUrl = rulesUrl;
    }
}
