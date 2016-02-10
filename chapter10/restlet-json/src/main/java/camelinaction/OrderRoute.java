package camelinaction;

import org.apache.camel.builder.RouteBuilder;

public class OrderRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // enable Jackson json type converter
        getContext().getProperties().put("CamelJacksonEnableTypeConverter", "true");
        // allow Jackson json to convert to pojo types also
        getContext().getProperties().put("CamelJacksonTypeConverterToPojo", "true");

        from("restlet:http://0.0.0.0:8080/orders?restletMethods=POST")
            .bean("orderService", "createOrder");

        from("restlet:http://0.0.0.0:8080/orders/{id}?restletMethods=GET")
            .bean("orderService", "getOrder(${header.id})");

        from("restlet:http://0.0.0.0:8080/orders?restletMethods=PUT")
            .bean("orderService", "updateOrder");

        from("restlet:http://0.0.0.0:8080/orders/{id}?restletMethods=DELETE")
            .bean("orderService", "cancelOrder(${header.id})");
    }
}
