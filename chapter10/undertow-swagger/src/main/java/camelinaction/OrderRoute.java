package camelinaction;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;

public class OrderRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // configure rest-dsl
        restConfiguration()
           // to use undertow component and run on port 8080
            .component("undertow").port(8080)
            // and enable json/xml binding mode
            .bindingMode(RestBindingMode.json_xml)
            // lets enable pretty printing json responses
            .dataFormatProperty("prettyPrint", "true")
            // lets enable swagger api
            .apiContextPath("api-doc")
            // and setup api properties
            .apiProperty("api.version", "2.0.0")
            .apiProperty("api.title", "Rider Auto Parts Order Services")
            .apiProperty("api.description", "Order Service that allows customers to submit orders and query status")
            .apiProperty("api.contact.name", "Rider Auto Parts");


        // error handling to return custom HTTP status codes for the various exceptions

        onException(OrderInvalidException.class)
            .handled(true)
            // use HTTP status 400 when input data is invalid
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
            .setBody(constant(""));

        onException(OrderNotFoundException.class)
            .handled(true)
            // use HTTP status 404 when data was not found
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(404))
            .setBody(constant(""));

        onException(Exception.class)
            .handled(true)
            // use HTTP status 500 when we had a server side error
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
            .setBody(simple("${exception.message}\n"));

        // rest services under the orders context-path
        rest("/orders").description("Order services")

            .get("/ping").apiDocs(false)
                .to("direct:ping")

            .get("{id}").outType(Order.class)
                .description("Service to get details of an existing order")
                .param().name("id").description("The order id").endParam()
                .responseMessage().code(200).message("The order with the given id").endResponseMessage()
                .responseMessage().code(404).message("Order not found").endResponseMessage()
                .responseMessage().code(500).message("Server error").endResponseMessage()
                .to("bean:orderService?method=getOrder(${header.id})")

            .post().type(Order.class).outType(String.class)
                .description("Service to submit a new order")
                .responseMessage()
                    .code(200).message("The created order id")
                .endResponseMessage()
                .responseMessage().code(400).message("Invalid input data").endResponseMessage()
                .responseMessage().code(500).message("Server error").endResponseMessage()
                .to("bean:orderService?method=createOrder")

            .put().type(Order.class)
                .description("Service to update an existing order")
                .responseMessage().code(400).message("Invalid input data").endResponseMessage()
                .responseMessage().code(500).message("Server error").endResponseMessage()
                .to("bean:orderService?method=updateOrder")

            .delete("{id}")
                .description("Service to cancel an existing order")
                .param().name("id").description("The order id").endParam()
                .responseMessage().code(404).message("Order not found").endResponseMessage()
                .responseMessage().code(500).message("Server error").endResponseMessage()
                .to("bean:orderService?method=cancelOrder(${header.id})");
    }
}
