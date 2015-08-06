package camelinaction;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * Main class to run the ping service
 */
public class PingServiceMain {

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        context.addRoutes(new PingService());

        context.start();

        System.out.println("Ping service running. Try sending a HTTP GET to http://localhost:8080/ping");
        System.out.println("Camel started use ctrl + c to stop.");

        Runtime.getRuntime().addShutdownHook(new ShutdownThread(context));

        Thread.sleep(99999999);
    }

}
