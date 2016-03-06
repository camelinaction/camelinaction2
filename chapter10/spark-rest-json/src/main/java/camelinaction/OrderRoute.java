package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

public class OrderRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // configure rest-dsl
        restConfiguration()
           // to use spark-rest component and run on port 8080
            .component("spark-rest").port(8080)
            // and enable json binding mode
            .bindingMode(RestBindingMode.json);

        // rest services under the orders context-path
        rest("/orders")
            .get("{id}").outType(Order.class)
                .to("bean:orderService?method=getOrder(${header.id})")
            .post().type(Order.class)
                .to("bean:orderService?method=createOrder")
            .put().type(Order.class)
                .to("bean:orderService?method=updateOrder")
            .delete("{id}")
                .to("bean:orderService?method=cancelOrder(${header.id})");
    }
}
