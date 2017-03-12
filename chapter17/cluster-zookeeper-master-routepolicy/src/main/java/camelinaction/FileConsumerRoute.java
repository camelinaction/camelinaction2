package camelinaction;

import org.apache.camel.builder.RouteBuilder;

public class FileConsumerRoute extends RouteBuilder {

    private int delay;
    private String name;

    public FileConsumerRoute(String name, int delay) {
        this.name = name;
        this.delay = delay;
    }

    @Override
    public void configure() throws Exception {
        // read files from the shared directory

        // its import to set the route to not auto startup
        // as we let the route policy start/stop the routes when it becomes a master/slave etc
        from("file:target/inbox?delete=true").noAutoStartup()
            // use the zookeeper master route policy in the clustered group
            // to run this route in master/slave mode
            .routePolicyRef("zookeeper-master-policy")
            .log(name + " - Received file: ${file:name}")
            .delay(delay)
            .log(name + " - Done file:     ${file:name}")
            .to("file:target/outbox");
    }

}
