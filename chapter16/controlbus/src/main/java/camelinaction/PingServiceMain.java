package camelinaction;

import org.apache.camel.main.Main;

/**
 * Main class to run the ping service which can be managed using the controlbus EIP
 */
public class PingServiceMain {

    public static void main(String[] args) throws Exception {
        // use Camel main to run Camel easily from Java main
        Main main = new Main();
        // enable support for graceful shutdown if the JVM terminates
        main.enableHangupSupport();

        // add the route
        main.addRouteBuilder(new PingService());

        System.out.println("Ping service running. Try sending a HTTP GET to http://localhost:8080/rest/ping");
        System.out.println("The route can be managed by sending any of the following:");
        System.out.println("\tTo stop the route\t\thttp://localhost:8080/rest/route/stop ");
        System.out.println("\tTo start the route\t\thttp://localhost:8080/rest/route/stop ");
        System.out.println("\tTo get the status of the route\thttp://localhost:8080/rest/route/status");
        System.out.println("\tTo get performance stats\thttp://localhost:8080/rest/route/stats");
        System.out.println("Camel started use ctrl + c to stop.");

        // run until the JVM terminates
        main.run();
    }

}
