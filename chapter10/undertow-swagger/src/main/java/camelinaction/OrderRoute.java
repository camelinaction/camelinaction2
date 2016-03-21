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
            .apiContextPath("api-doc");


        // error handling to return custom HTTP status codes for the various exceptions

        onException(OrderNotFoundException.class)
            .handled(true)
            // use HTTP status 204 when data was not found
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(204))
            .setBody(constant(""));

        onException(OrderInvalidException.class)
            .handled(true)
            // use HTTP status 400 when input data is invalid
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
            .setBody(constant(""));

        onException(Exception.class)
            .handled(true)
            // use HTTP status 500 when we had a server side error
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
            .setBody(simple("${exception.message}\n"));

        // rest services under the orders context-path
        rest("/orders").description("Order services")

            .get("{id}").outType(Order.class)
                .description("Service to get details of an existing order")
                .param().name("id").description("The order id").endParam()
                .to("bean:orderService?method=getOrder(${header.id})")

            .post().type(Order.class).outType(String.class)
                .description("Service to submit a new order")
                .responseMessage().code(200).message("The created order id").endResponseMessage()
                .to("bean:orderService?method=createOrder")

            .put().type(Order.class)
                .description("Service to update an existing order")
                .to("bean:orderService?method=updateOrder")

            .delete("{id}")
                .description("Service to cancel an existing order")
                .param().name("id").description("The order id").endParam()
                .to("bean:orderService?method=cancelOrder(${header.id})");
    }
}
