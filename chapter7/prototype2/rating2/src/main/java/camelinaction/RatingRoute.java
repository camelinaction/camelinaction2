package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RatingRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // notice that we have also configured rest in the application.properties file
        restConfiguration()
            // turn on json binding in rest-dsl
            .bindingMode(RestBindingMode.json);

        // define the rest service
        rest("/ratings/{ids}").produces("application/json")
            .get().to("bean:ratingService");
    }
}
