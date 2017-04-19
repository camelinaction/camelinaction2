package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ServerRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("servlet:hello")
            .transform().constant("Hello from foo server on port 8081");
    }
}
