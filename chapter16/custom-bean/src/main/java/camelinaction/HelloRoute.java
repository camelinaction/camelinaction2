package camelinaction;

import org.apache.camel.builder.RouteBuilder;

/**
 * A simple Camel route to call the bean
 */
public class HelloRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // trigger the route every 5th second
        from("timer:foo?period=5s")
            // call the hello bean say method
            .beanRef("hello", "say")
            // log the response from the bean
            .log("${body}");
    }
}
