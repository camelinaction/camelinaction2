package camelinaction;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpOperationFailedException;

/**
 * The route for the use case
 */
public class UseCaseRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        getContext().setTracing(true);

        // general error handler
        errorHandler(defaultErrorHandler()
            .maximumRedeliveries(5)
            .redeliveryDelay(2000)
            .retryAttemptedLogLevel(LoggingLevel.WARN));

        // in case of a http exception then retry at most 3 times
        // and if exhausted then upload using ftp instead
        onException(HttpOperationFailedException.class).maximumRedeliveries(3)
            .handled(true)
            // use file instead of ftp as its easier to play with
            .to("file:target/ftp/upload");

        // poll files every 5th second and send them to the HTTP server
        from("file:target/rider?delay=5000&readLock=none")
            .to("http://localhost:8765/rider");
    }
}
