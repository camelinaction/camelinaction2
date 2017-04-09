package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.Main;

/**
 * A Camel Router
 */
public class MyRouteBuilder extends RouteBuilder {

    /**
     * A main() so we can easily run these routing rules in our IDE
     */
    public static void main(String... args) throws Exception {
        Main.main(args);
    }

    /**
     * Lets configure the Camel routing rules using Java code...
     */
    public void configure() {
        from("ftp://rider@localhost:21000/order?password=secret")
          .to("jms:incomingOrders");

        from("cxf:bean:orderEndpoint")
          .to("jms:incomingOrders");

        from("jms:incomingOrders").to("log:camelinaction.order");
    }
}
