package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * A route which is routing to foo.
 */
@Component
public class FooRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("file:target/inbox")
                .to("file:target/foo");
    }

}
