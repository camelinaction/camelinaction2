package camelinaction;

import org.apache.camel.builder.RouteBuilder;

/**
 * A basic route that exposes a HTTP service
 */
public class HelloRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("jetty:http://localhost:8080/hello")
            .transform().simple("Hello from Camel");
    }
}
