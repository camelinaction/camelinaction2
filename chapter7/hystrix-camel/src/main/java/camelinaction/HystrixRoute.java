package camelinaction;

import org.apache.camel.builder.RouteBuilder;

public class HystrixRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:start")
            .hystrix()
                .to("bean:counter")
                .log("After calling counter service: ${body}")
            .end();
    }
}
