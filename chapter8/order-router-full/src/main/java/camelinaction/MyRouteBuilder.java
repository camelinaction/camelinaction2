package camelinaction;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

/**
 * A Camel Router
 */
public class MyRouteBuilder extends RouteBuilder {

    /**
     * Lets configure the Camel routing rules using Java code...
     */
    public void configure() {
        from("ftp://rider@localhost:21000/order?password=secret&delete=true")
          .to("log:camelinaction.order.ftp")
          .to("jms:incomingOrders");

        from("cxf:bean:orderEndpoint")
          .convertBodyTo(String.class)
          .to("log:camelinaction.order.ws")
          .inOnly("jms:incomingOrders")
          .transform(constant("OK"));

        from("jms:incomingOrders")
          .to("log:camelinaction.order.jms");
    }
}
