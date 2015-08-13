package camelinaction;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * A main app to start this example.
 */
public class ERPMain {

    public static void main(String[] args) throws Exception {
        ERPMain client = new ERPMain();
        System.out.println("Starting ERPMain... press ctrl + c to stop it");
        client.start();
        System.out.println("... started.");
        Thread.sleep(99999999);
    }

    private void start() throws Exception {
        CamelContext camel = new DefaultCamelContext();

        // add our custom component
        camel.addComponent("erp", new ERPComponent());

        // add the route
        camel.addRoutes(new ERPRoute());

        // and start Camel
        camel.start();
    }

}
