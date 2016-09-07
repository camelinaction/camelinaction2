package camelinaction;

import org.apache.camel.EndpointInject;
import org.apache.camel.FluentProducerTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring REST endpoint
 */
@RestController
@RequestMapping("/spring")
public class HelloRestController {

    /**
     * Inject Camel producer to use camel-geocoder to find location where we are
     */
    @EndpointInject(uri = "geocoder:address:current")
    private FluentProducerTemplate producer;

    /**
     * HTTP GET method
     */
    @RequestMapping(method = RequestMethod.GET, value = "/hello",
                    produces = "text/plain")
    public String hello() {
        // call Camel to find our location, the returned string is in JSon format
        String where = producer.request(String.class);

        return "Hello from Spring Boot and Camel. We are at: " + where;
    }
}

