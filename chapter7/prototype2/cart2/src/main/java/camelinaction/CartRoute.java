package camelinaction;

import javax.inject.Singleton;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

/**
 * Shopping Cart REST service using Camel rest-dsl (to be covered in chapter 10)
 */
@Singleton
public class CartRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // use jetty for rest service
        restConfiguration("jetty").port("{{port}}").contextPath("api")
                // turn on json binding
                .bindingMode(RestBindingMode.json)
                // turn off binding error on empty beans
                .dataFormatProperty("disableFeatures", "FAIL_ON_EMPTY_BEANS")
                // enable swagger api documentation
                .apiContextPath("api-doc")
                .enableCORS(true);

        // define the rest service
        rest("/cart").consumes("application/json").produces("application/json")
            // get returns List<CartDto>
            .get().outType(CartDto[].class).description("Returns the items currently in the shopping cart")
                .to("bean:cart?method=getItems")
            // get accepts CartDto
            .post().type(CartDto.class).description("Adds the item to the shopping cart")
                .to("bean:cart?method=addItem")
            .delete().description("Removes the item from the shopping cart")
                .param().name("itemId").description("Id of item to remove").endParam()
                .to("bean:cart?method=removeItem");
    }
}
