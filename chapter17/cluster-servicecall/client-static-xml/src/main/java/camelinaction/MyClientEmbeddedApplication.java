package camelinaction;

import org.apache.camel.spring.Main;

/**
 * Class to run the Camel client which uses a static server list
 * where the Service Call configuration is embedded in the route
 */
public class MyClientEmbeddedApplication {

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.setApplicationContextUri("static-route-embedded.xml");
        main.run();
    }

}
