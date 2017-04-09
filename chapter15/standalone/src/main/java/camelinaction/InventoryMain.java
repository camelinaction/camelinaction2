package camelinaction;

import org.apache.camel.spring.Main;

/**
 * A Main class to run our Camel application standalone
 */
public class InventoryMain {

    public static void main(String[] args) throws Exception {
        // use the Main class from camel-spring
        Main main = new Main();
        // to load Spring XML file
        main.setApplicationContextUri("META-INF/spring/camel-context.xml");
        // echo to console how you can stop
        System.out.println("\n\nApplication has now been started. You can press ctrl + c to stop.\n\n");
        // and run (will wait until you stop with ctrl + c)
        main.run();
    }

}
