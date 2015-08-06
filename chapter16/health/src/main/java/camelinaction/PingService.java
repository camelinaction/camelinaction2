package camelinaction;

import org.apache.camel.builder.RouteBuilder;

/**
 * A route which exposes a ping service over HTTP.
 */
public class PingService extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("jetty:http://0.0.0.0:8080/ping").transform(constant("PONG\n"));
    }

}
