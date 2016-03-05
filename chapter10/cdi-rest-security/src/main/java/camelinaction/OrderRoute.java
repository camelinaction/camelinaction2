package camelinaction;

import org.apache.camel.builder.RouteBuilder;

public class OrderRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // configure rest-dsl
        restConfiguration()
           // to use jetty component and run on port 8080
            .component("jetty").port(8080)
            // use a smaller thread pool in jetty as we do not have so high demand yet
            .componentProperty("minThreads", "1")
            .componentProperty("maxThreads", "8")
            // to setup jetty to use the security handler
            .endpointProperty("handlers", "#securityHandler");

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
