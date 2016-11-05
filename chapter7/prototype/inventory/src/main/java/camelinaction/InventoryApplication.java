package camelinaction;

import org.apache.camel.spring.Main;

/**
 * Main class to run this application.
 */
public class InventoryApplication {

    private static Main main;

    public static void main(String[] args) throws Exception {
        main = new Main();
        main.setApplicationContextUri("broker.xml;camel.xml");
        main.run();
    }
}
