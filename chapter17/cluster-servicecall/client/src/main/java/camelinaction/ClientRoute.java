package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.consul.cloud.ConsulServiceDiscovery;
import org.springframework.stereotype.Component;

@Component
public class ClientRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("timer:trigger?period=2000")
            .serviceCall("hello-service/camel/hello")
            .log("Response ${body}");
    }
}
