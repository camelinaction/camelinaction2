package camelinaction;

import org.apache.camel.builder.RouteBuilder;

/**
 * The Hello World example of integration kits, which is moving a file.
 */
public class FileMoveRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("file://target/inbox")
                .to("file://target/outbox");
    }

}
