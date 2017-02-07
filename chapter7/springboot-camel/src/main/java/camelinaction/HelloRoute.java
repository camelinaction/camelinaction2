package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * A Camel route in Spring Boot.
 *
 * Notice that we use @Component on the class to make the route automatic discovered by Spring Boot
 */
@Component
public class HelloRoute extends RouteBuilder {

    @Bean
    ServletRegistrationBean camelServlet() {
        // use a @Bean to register the Camel servlet which we need to do
        // because we want to use the camel-servlet component for the Camel REST service
        ServletRegistrationBean mapping = new ServletRegistrationBean();
        mapping.setName("CamelServlet");
        mapping.setLoadOnStartup(1);
        // CamelHttpTransportServlet is the name of the Camel servlet to use
        mapping.setServlet(new CamelHttpTransportServlet());
        mapping.addUrlMappings("/camel/*");
        return mapping;
    }

    @Override
    public void configure() throws Exception {
        // define a Camel REST service using the rest-dsl
        // where we define a GET /hello as a service that routes to the hello route
        // we will cover rest-dsl in chapter 10

        rest("/").produces("text/plain")
            .get("hello")
            .to("direct:hello");

        from("direct:hello")
            .to("geocoder:address:current")
            .transform().simple("Hello from Spring Boot and Camel. We are at: ${body}");
    }
}
