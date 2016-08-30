package camelinaction;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.camel.Endpoint;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.Uri;

/**
 * A basic route that exposes a HTTP service.
 * This route uses CDI dependency injection.
 */
@Singleton
public class HelloRoute extends RouteBuilder {

    // use CDI @Inject to inject the bean of type HelloBean
    @Inject
    private HelloBean hello;

    // use camel-cdi @Uri to inject the endpoint
    @Inject @Uri("jetty:http://localhost:8080/hello")
    private Endpoint jetty;

    @Override
    public void configure() throws Exception {
        from(jetty)
            // call the sayHello method on the hello bean
            .bean(hello, "sayHello");
    }
}
