package com.camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * Camel as client that calls the hello service using a timer every 2nd seconds and logs the response
 */
@Component
public class HelloRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("timer:foo?period=2000")
            .hystrix()
                // call the service using its DNS name and port number
                .to("netty4-http:http://helloswarm-kubernetes:8080?disconnect=true&keepAlive=false")
            .onFallback()
                .transform().constant("Cannot call downstream service")
            .end()
            .log("${body}");
    }
}



