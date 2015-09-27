package camelinaction.server;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;
import org.apache.camel.main.MainListenerSupport;

public class JmxCamelServer extends Main {

    public static void main(String[] args) throws Exception {
        Main main = new Main();

        // enable remote JMX management connector
        main.addMainListener(new MainListenerSupport() {
            @Override
            public void configure(CamelContext context) {
                context.getManagementStrategy().getManagementAgent().setCreateConnector(true);
            }
        });

        // add a little route
        main.addRouteBuilder(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("timer:foo?period=1000").log("I am running");
            }
        });

        // allow to stop nicely when jvm terminates
        main.enableHangupSupport();

        // keep running
        main.run();
    }

}
