package camelinaction;

import org.apache.camel.main.Main;

/**
 * Running Camel standalone using a Main class
 */
public class HelloMain {

    public static void main(String[] args) throws Exception {
        // use org.apache.camel.main.Main to make it easier to run Camel standalone
        Main main = new Main();
        // add the routes
        main.addRouteBuilder(new HelloRoute());
        // run the application (keep it running)
        main.run();
    }

}
