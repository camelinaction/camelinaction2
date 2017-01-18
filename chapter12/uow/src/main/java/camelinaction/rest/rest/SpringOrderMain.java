package camelinaction.rest.rest;

import org.apache.camel.spring.Main;

/**
 * Main class using camel-spring to boot the order service using XML DSL.
 */
public class SpringOrderMain {

    public static void main(String[] args) throws Exception {
        Main main = new Main();

        main.setApplicationContextUri("camelinaction/rest/restOnCompletion.xml");

        System.out.println("=======================================================");
        System.out.println("Starting HTTP server on port 8080");
        System.out.println("Hit url http://localhost:8080/service/order/123");
        System.out.println("... and press CTRL + C to exit");
        System.out.println("=======================================================");

        main.run();
    }

}
