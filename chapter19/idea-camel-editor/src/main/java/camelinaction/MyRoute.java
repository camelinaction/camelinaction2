package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.annotation.Bean;

/**
 * A simple Camel route
 */
public class MyRoute extends RouteBuilder {

    @Override
    public void configure() {
        from("timer:trigger?delay=500")
                .transform().simple("ref:myBean")
                .to("log:out");
    }

    @Bean
    String myBean() {
        return "I'm Spring bean!";
    }

}
