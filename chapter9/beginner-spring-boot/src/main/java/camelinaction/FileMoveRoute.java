package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * The Hello World example of integration kits, which is moving a file.
 */
@Component
public class FileMoveRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("file://target/inbox")
                .to("file://target/outbox");
    }

}
