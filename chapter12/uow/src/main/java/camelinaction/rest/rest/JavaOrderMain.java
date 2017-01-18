package camelinaction.rest.rest;

import org.apache.camel.main.Main;

/**
 * Main class using camel-core to boot the order service using Java DSL.
 */
public class JavaOrderMain {

    public static void main(String[] args) throws Exception {
        Main main = new Main();

        main.bind("orderService", new OrderService());
        main.bind("tokenService", new TokenService());

        main.addRouteBuilder(new OrderRoute());

        System.out.println("=======================================================");
        System.out.println("Starting HTTP server on port 8080");
        System.out.println("Hit url http://localhost:8080/service/order/123");
        System.out.println("... and press CTRL + C to exit");
        System.out.println("=======================================================");

        main.run();
    }

}
