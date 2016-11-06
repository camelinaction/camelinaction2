package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RatingRoute extends RouteBuilder {

    @Bean
    ServletRegistrationBean camelServlet() {
        // TODO: Camel 2.19 should support this OOTB
        // use a @Bean to register the Camel servlet which we need to do
        // because we want to use the camel-servlet component for the Camel REST service
        ServletRegistrationBean mapping = new ServletRegistrationBean();
        mapping.setName("CamelServlet");
        mapping.setLoadOnStartup(1);
        // CamelHttpTransportServlet is the name of the Camel servlet to use
        mapping.setServlet(new CamelHttpTransportServlet());
        mapping.addUrlMappings("/api/*");
        return mapping;
    }

    @Override
    public void configure() throws Exception {

        // use servlet for rest service
        restConfiguration("servlet").contextPath("api").port(8080)
            // turn on json binding
            .bindingMode(RestBindingMode.json);

        // define the rest service
        rest("/ratings/{ids}").produces("application/json")
            .get().to("bean:ratingService");
    }
}
