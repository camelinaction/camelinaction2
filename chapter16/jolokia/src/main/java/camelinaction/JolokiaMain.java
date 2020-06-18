package camelinaction;

import org.apache.camel.main.Main;

public class JolokiaMain {

    public static void main(String[] args) throws Exception {
        // use Camel main to run Camel easily from Java main
        Main main = new Main();

        // add the route
        main.configure().addRoutesBuilder(new JolokiaRoute());

        System.out.println("Jolokia running. Try sending a HTTP GET to http://localhost:8778/jolokia/");
        System.out.println("Camel started use ctrl + c to stop.");

        // run until the JVM terminates
        main.run();
    }

}
