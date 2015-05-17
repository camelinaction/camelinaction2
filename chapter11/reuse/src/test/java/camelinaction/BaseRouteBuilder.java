package camelinaction;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public abstract class BaseRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // this is the common error handler we want to reuse in all routes
        errorHandler(deadLetterChannel("mock:dead")
                .maximumRedeliveries(2)
                .redeliveryDelay(1000)
                .retryAttemptedLogLevel(LoggingLevel.WARN));
    }
}
