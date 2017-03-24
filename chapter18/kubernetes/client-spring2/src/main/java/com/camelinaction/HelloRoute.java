package com.camelinaction;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * Camel as client that calls the hello service using a timer every 2nd seconds and logs the response
 */
@Component
public class HelloRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // use retry pattern to retry calling the hello service
        // perform up till 5 retries with 3 second delay,
        // and log at INFO level when an attempt is performed
        errorHandler(defaultErrorHandler()
            .retryAttemptedLogLevel(LoggingLevel.INFO)
            .maximumRedeliveries(5)
            .redeliveryDelay(3000));

        // use synchronous so we only have one concurrent thread from the timer
        from("timer:foo?period=2000&synchronous=true")
            // call the service using {{service:name}}
            .to("netty4-http://{{service:helloswarm-kubernetes}}?disconnect=true&keepAlive=false")
            .log("${body}");
    }
}



