package camelinaction;

import org.apache.camel.builder.RouteBuilder;

public class OrderRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // configure rest-dsl
        restConfiguration()
           // to use spark-rest component and run on port 8080
            .component("spark-rest").port(8080);

        // rest services under the orders context-path
        rest("/orders")
            .get("{id}")
                .to("bean:orderService?method=getOrder(${header.id})")
            .post()
                .to("bean:orderService?method=createOrder")
            .put()
                .to("bean:orderService?method=updateOrder")
            .delete("{id}")
                .to("bean:orderService?method=cancelOrder(${header.id})");
    }
}
