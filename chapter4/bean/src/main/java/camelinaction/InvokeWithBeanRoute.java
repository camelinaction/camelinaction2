package camelinaction;

import org.apache.camel.builder.RouteBuilder;

/**
 * Using a bean in the route to invoke HelloBean.
 */
public class InvokeWithBeanRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:hello")
            // instantiate HelloBean once, and reuse and invoke the hello bean
            .bean(HelloBean.class, "hello");
    }
}
