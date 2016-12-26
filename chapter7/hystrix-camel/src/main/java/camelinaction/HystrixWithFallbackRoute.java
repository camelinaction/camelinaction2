package camelinaction;

import org.apache.camel.builder.RouteBuilder;

public class HystrixWithFallbackRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:start")
            .hystrix()
                // protect calling the counter service using Hystrix
                .to("bean:counter")
                // notice you can have more Camel EIPs/nodes here
                // .to("bean:anotherBean")
            .onFallback()
                // use a constant message as fallback
                .transform(constant("No Counter"))
            .end()
            // run outside hystrix
            .log("After calling counter service: ${body}");
    }
}
