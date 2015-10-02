package camelinaction;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class SchedulerExample {

    public static void main(String args[]) throws Exception {
        // create CamelContext
        CamelContext context = new DefaultCamelContext();

        // add our route to the CamelContext
        context.addRoutes(new RouteBuilder() {
            public void configure() {
                from("scheduler://myScheduler?delay=2000")
                .setBody().simple("Current time is ${header.CamelTimerFiredTime}")
                .to("stream:out");
            }
        });

        // start the route and let it do its work
        context.start();
        Thread.sleep(5000);

        // stop the CamelContext
        context.stop();
    }
    
}
