package camelinaction.server;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;
import org.apache.camel.main.MainListenerSupport;

public class JmxCamelServer extends Main {

    public static void main(String[] args) throws Exception {
        Main main = new Main();

        // add a little route
        main.configure().addRoutesBuilder(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("timer:foo?period=1000").log("I am running");
            }
        });

        // keep running
        main.run();
    }

}
