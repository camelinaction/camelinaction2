package camelinaction;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.camel.builder.RouteBuilder;

/**
 * A basic route that exposes a HTTP service
 */
@Singleton
public class HelloRoute extends RouteBuilder {

    // use CDI @Inject to inject the bean of type HelloBean
    @Inject
    private HelloBean hello;

    @Override
    public void configure() throws Exception {
        from("jetty:http://localhost:8080/hello")
            // call the sayHello method on the hello bean
            .bean(hello, "sayHello");
    }
}
