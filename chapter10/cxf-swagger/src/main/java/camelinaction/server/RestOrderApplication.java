package camelinaction.server;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import camelinaction.RestOrderService;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.jaxrs.swagger.Swagger2Feature;

/**
 * JAX-RS application that setup the JAX-RS resources:
 * - RestOrderService
 * - Swagger
 * - Jackson for JSOn
 * - Optional logging
 */
@ApplicationPath("/")
public class RestOrderApplication extends Application {

    private final RestOrderService orderService;

    public RestOrderApplication(RestOrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public Set<Object> getSingletons() {
        Swagger2Feature swagger = new Swagger2Feature();
        swagger.setBasePath("/");
        swagger.setHost("localhost:9000");
        swagger.setTitle("Order Service");
        swagger.setDescription("Rider Auto Parts Order Service");
        swagger.setVersion("2.0.0");
        swagger.setContact("rider@autoparts.com");
        swagger.setPrettyPrint(true);

        Set<Object> answer = new HashSet<>();
        answer.add(orderService);
        answer.add(new JacksonJsonProvider());
        answer.add(swagger);
        // to turn on verbose logging
        answer.add(new LoggingFeature());
        return answer;
    }

}
