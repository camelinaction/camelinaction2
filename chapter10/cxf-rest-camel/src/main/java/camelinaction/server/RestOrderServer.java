package camelinaction.server;

import java.io.Console;

import camelinaction.RestOrderService;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
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

        // create a Camel route that routes the REST services
        OrderRoute route = new OrderRoute();
        route.setOrderService(dummy);

        // create CamelContext and add the route
        CamelContext camel = new DefaultCamelContext();
        camel.addRoutes(route);

        // create a ProducerTemplate that the CXF REST service will use to integrate with Camel
        ProducerTemplate producer = camel.createProducerTemplate();

        // create CXF REST service and inject the Camel ProducerTemplate
        // which we use to call the Camel route
        RestOrderService rest = new RestOrderService();
        rest.setProducerTemplate(producer);

        // setup Apache CXF REST server on port 9000
        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.setResourceClasses(RestOrderService.class);
        sf.setResourceProvider(RestOrderService.class, new SingletonResourceProvider(rest));
        // to use jackson for json
        sf.setProvider(JacksonJsonProvider.class);
        sf.setAddress("http://localhost:9000/");

        // create the CXF server
        Server server = sf.create();

        // start Camel and CXF (non blocking)
        camel.start();
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

        // stop Camel and CXF server
        camel.stop();
        server.stop();
        System.exit(0);
    }

}
