package camelinaction;

import java.io.IOException;

import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpOperationFailedException;

/**
 * The use case route.
 */
public class UseCaseRoute extends RouteBuilder {

    // inject the endpoints to use

    @EndpointInject(ref = "fileEndpoint")
    private Endpoint file;

    @EndpointInject(ref = "httpEndpoint")
    private Endpoint http;

    @EndpointInject(ref = "ftpEndpoint")
    private Endpoint ftp;

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
        onException(IOException.class, HttpOperationFailedException.class)
            .maximumRedeliveries(3)
            .handled(true)
            .to(ftp);

        // poll files send them to the HTTP server
        from(file).to(http);
    }

}
