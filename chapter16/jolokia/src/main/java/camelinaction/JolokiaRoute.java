package camelinaction;

import org.apache.camel.builder.RouteBuilder;

public class JolokiaRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("timer:foo?period=5s")
            .log("I am running.");
    }
}
