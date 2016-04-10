package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;

/**
 * A simple hello route that returns a hello message
 */
@ContextName("helloCamel")
public class HelloRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:hello")
            .transform().simple("Hello ${body}");
    }

}
