package camelinaction;

import javax.inject.Singleton;

import org.apache.camel.builder.RouteBuilder;

/**
 * A simple hello route that returns a hello message
 */
@Singleton
public class HelloRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:hello")
            .transform().simple("Hello ${body}");
    }

}
