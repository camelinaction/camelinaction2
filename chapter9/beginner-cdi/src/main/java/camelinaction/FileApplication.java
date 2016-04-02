package camelinaction;

import org.apache.camel.cdi.Main;

/**
 * A simple Camel application that runs this CDI based application.
 */
public class FileApplication {

    /**
     * Main class to run this example, such as from your Java editor
     */
    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.run();
    }
}
