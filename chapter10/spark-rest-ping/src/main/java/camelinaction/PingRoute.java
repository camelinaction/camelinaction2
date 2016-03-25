package camelinaction;

import org.apache.camel.builder.RouteBuilder;

public class PingRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // configure rest-dsl
        restConfiguration()
            // to use spark-rest component and run on port 9090
            .component("spark-rest").port(9090)
            // enable api-docs
            .apiContextPath("api-doc")
            // enable CORS on rest services so they can be called from swagger UI
            .enableCORS(true)
            // enable CORS in the api-doc as well so the swagger UI can view it
            .apiProperty("cors", "true");

        // pong rest service
        rest("/ping").get().route().transform().constant("{ \"reply\": \"pong\" }");

    }
}
