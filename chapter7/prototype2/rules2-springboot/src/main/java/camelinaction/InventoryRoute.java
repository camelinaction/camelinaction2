package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.springframework.stereotype.Component;

/**
 * Camel route that calls the legacy system.
 * <p/>
 * This implementation is identical to the code in the WildFly Swarm example.
 * From Camel point of view its similar to run in WildFly or Spring Boot.
 * The only change is that Spring Boot uses <tt>@Component</tt> and WildFly Swarm
 * uses <tt>@ApplicationScoped</tt> as the class annotation.
 */
@Component
public class InventoryRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        JaxbDataFormat jaxb = new JaxbDataFormat();
        jaxb.setContextPath("camelinaction");

        from("direct:inventory")
            .log("Calling inventory service using JMS")
            .hystrix()
                // call the legacy system using JMS (via activemq)
                .to("activemq:queue:inventory")
                // the returned data is in XML format so convert that to POJO using JAXB
                .unmarshal(jaxb)
            .onFallback()
                .log("Circuit breaker failed so using fallback response")
                // fallback and read inventory from classpath which is in XML format
                .transform().constant("resource:classpath:fallback-inventory.xml");
    }

}
