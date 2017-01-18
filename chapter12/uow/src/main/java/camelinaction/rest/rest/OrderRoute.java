package camelinaction.rest.rest;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

public class OrderRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // configure rest to use netty4-http component as the HTTP server component
        // enable json binding mode so we can leverage camel-jackson to bind json to/from pojos
        restConfiguration().component("netty4-http").bindingMode(RestBindingMode.json)
                // expose the service as localhost:8080/service
                .host("localhost").port(8080).contextPath("service");

        // include a token id header, which we insert before the consumer completes
        // (and therefore before the consumer writes the response to the caller)
        onCompletion().modeBeforeConsumer()
                .setHeader("Token").method("tokenService");

        // use rest-dsl to define the rest service to lookup orders
        rest()
            .get("/order/{id}")
                .to("bean:orderService?method=getOrder");

    }
}
