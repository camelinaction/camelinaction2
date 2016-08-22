package camelinaction;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * Running Camel by:
 * - creating a CamelContext
 * - add route(s)
 * - start the CamelContext
 */
public class HelloCamel {

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        context.addRoutes(new HelloRoute());
        context.start();

        // keep the JVM running (a bit of a hack)
        Thread.sleep(Integer.MAX_VALUE);
    }

}
