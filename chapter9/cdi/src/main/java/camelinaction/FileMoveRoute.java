package camelinaction;

import javax.inject.Singleton;

import org.apache.camel.builder.RouteBuilder;

/**
 * The Hello World example of integration kits, which is moving a file.
 */
@Singleton
public class FileMoveRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("file:target/inbox")
            .to("file:target/outbox");
    }

}
