package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class HelloRoute extends RouteBuilder {

    @Bean
    ServletRegistrationBean camelServlet() {
        ServletRegistrationBean mapping = new ServletRegistrationBean();
        mapping.setName("CamelServlet");
        mapping.setLoadOnStartup(1);
        mapping.setServlet(new CamelHttpTransportServlet());
        mapping.addUrlMappings("/camel/*");
        return mapping;
    }

    @Override
    public void configure() throws Exception {
        restConfiguration("servlet");

        rest("/")
            .get("hello")
                .route()
                .to("geocoder:address:current")
                .transform().simple("Hello from Spring Boot and Camel. We are at: ${body}");
    }
}
