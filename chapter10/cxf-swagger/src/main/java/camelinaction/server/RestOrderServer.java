package camelinaction.server;

import java.io.Console;

import javax.servlet.Servlet;

import camelinaction.RestOrderService;
import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * A main class to start Apache CXF with our REST service {@link RestOrderService} using Swagger.
 * <p/>
 * This main class uses Apache CXF in pure Java code without any XML configuration.
 * Notice this configuration requires a bit of Java code, and more advanced configuration
 * and usage of Apache CXF often involves configuration in XML files
 * (due CXF was very Spring XML in the start of its lifetime).
 * <p/>
 * This server uses an embedded Jetty server to setup a CXF servlet that bootstraps
 * a JAX-RS application {@link RestOrderApplication}.
 */
public class RestOrderServer {

    public static void main(String[] args) throws Exception {
        // create dummy backend
        DummyOrderService dummy = new DummyOrderService();
        dummy.setupDummyOrders();

        // create rider order service with dummy backend
        RestOrderService orderService = new RestOrderService();
        orderService.setOrderService(dummy);

        // create JAX-RS application with our rider order serivce
        RestOrderApplication app = new RestOrderApplication(orderService);

        // setup servlet holder with a CXF jax-rs servlet to handle the app
        Servlet servlet = new CXFNonSpringJaxrsServlet(app);
        ServletHolder holder = new ServletHolder(servlet);
        holder.setName("rider");
        holder.setForcedPath("/");
        ServletContextHandler context = new ServletContextHandler();
        context.addServlet(holder, "/*");

        // create and start the jetty server (non blocking)
        Server server = new Server(9000);
        server.setHandler(context);
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

        // stop jetty server
        server.stop();
        System.exit(0);
    }

}
