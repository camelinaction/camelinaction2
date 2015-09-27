package camelinaction;

import org.apache.camel.builder.RouteBuilder;

/**
 * The ping service route, which is using rest-dsl to setup the ping service as a REST service.
 * In addition there is REST service to control the route such as starting and stopping and more.
 */
public class PingService extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // use the restlet component on port 8080 as the REST server
        // no need for binding to json/jaxb as the rest services are using plain text
        restConfiguration().component("restlet").port(8080);

        rest("/rest").consumes("application/text").produces("application/text")

            // ping rest service
            .get("ping")
                .route().routeId("ping")
                .transform(constant("PONG\n"))
            .endRest()

            // controlbus to start/stop the route
            .get("route/{action}")
                // use dynamic-to so the action is provided from the url
                .toD("controlbus:route?routeId=ping&action=${header.action}");

    }

}
