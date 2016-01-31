package camelinaction.server;

import java.io.Console;

import camelinaction.RestOrderService;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;

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
