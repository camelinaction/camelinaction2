package camelinaction;

import org.apache.camel.main.Main;

/**
 * Class to run the Camel client which uses a static server list
 * where the Service Call configuration is embedded in the route
 */
public class MyClientEmbeddedApplication {

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.addRouteBuilder(new MyStaticRouteEmbedded());
        main.run();
    }

}
