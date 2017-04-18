package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ServerRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("undertow:http://localhost:8082")
            .transform().constant("Hello from bar server on port 8082");
    }
}
