package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;

/**
 * The Hello World example of integration kits, which is moving a file.
 */
@ContextName("helloCamel")
public class FileMoveRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("file:target/inbox")
            .to("file:target/outbox");
    }

}
