package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * A Camel route in Spring Boot.
 *
 * Notice that we use @Component on the class to make the route automatic discovered by Spring Boot
 */
@Component
public class HelloRoute extends RouteBuilder {

    // see the application.properties file how we map the context-path
    // of the camel-servlet that the rest-dsl will use

    @Override
    public void configure() throws Exception {
        // define a Camel REST service using the rest-dsl
        // where we define a GET /hello as a service that routes to the hello route
        // we will cover rest-dsl in chapter 10

        rest("/").produces("text/plain")
            .get("hello")
            .to("direct:hello");

        from("direct:hello")
            .to("geocoder:address:current")
            .transform().simple("Hello from Spring Boot and Camel. We are at: ${body}");
    }
}
