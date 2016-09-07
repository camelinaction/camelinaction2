package camelinaction;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring REST endpoint
 */
@RestController
@RequestMapping("/")
public class HelloRestController {

    /**
     * HTTP GET method
     */
    @RequestMapping(method = RequestMethod.GET, value = "/hello",
                    produces = "text/plain")
    public String hello() {
        return "Hello from Spring Boot";
    }
}

