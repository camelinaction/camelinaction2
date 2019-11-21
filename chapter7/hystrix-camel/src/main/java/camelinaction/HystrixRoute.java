package camelinaction;

import org.apache.camel.builder.RouteBuilder;

public class HystrixRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:start")
            .circuitBreaker()
                // protect calling the counter service using Hystrix
                .to("bean:counter")
                // notice you can have more Camel EIPs/nodes here
                // .to("bean:anotherBean")
            .end()
            // run outside hystrix
            .log("After calling counter service: ${body}");
    }
}
