package camelinaction;

import org.apache.camel.spring.Main;

/**
 * Class to run the Camel client
 */
public class MyClientApplication {

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        // the Camel application is configured in XML DSL in this file
        main.setApplicationContextUri("mycamel.xml");
        main.run();
    }

}
