package camelinaction.server;

import java.io.Console;

import camelinaction.RestOrderService;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;

/**
 * A main class to start Apache CXF with our REST service {@link RestOrderService}.
 * <p/>
 * This main class uses Apache CXF in pure Java code without any XML configuration.
 * Notice this configuration requires a bit of Java code, and more advanced configuration
 * and usage of Apache CXF often involves configurating in XML files
 * (due CXF was very Spring XML in the start of its lifetime).
 */
public class RestOrderServer {

    public static void main(String[] args) throws Exception {
        // create dummy backend
        DummyOrderService dummy = new DummyOrderService();
        dummy.setupDummyOrders();

        // create CXF REST service and inject the dummy backend
        RestOrderService rest = new RestOrderService();
        rest.setOrderService(dummy);

        // setup Apache CXF REST server on port 9000
        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.setResourceClasses(RestOrderService.class);
        sf.setResourceProvider(RestOrderService.class, new SingletonResourceProvider(rest));
        sf.setAddress("http://localhost:9000/");

        // create and start the CXF server (non blocking)
        Server server = sf.create();
        server.start();

        // keep the JVM running
        Console console = System.console();
        System.out.println("Server started on http://localhost:9000/");
        System.out.println("");

        // If you run the main class from IDEA/Eclipse then you may not have a console, which is null)
        if (console != null) {
            System.out.println("  Press ENTER to stop server");
            console.readLine();
        } else {
            System.out.println("  Stopping after 5 minutes or press ctrl + C to stop");
            Thread.sleep(5 * 60 * 1000);
        }

        // stop CXF server
        server.stop();
        System.exit(0);
    }

}
