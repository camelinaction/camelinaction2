package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * A route which is routing to bar.
 */
@Component
public class BarRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("file:target/inbox")
            .to("file:target/bar");
    }

}
