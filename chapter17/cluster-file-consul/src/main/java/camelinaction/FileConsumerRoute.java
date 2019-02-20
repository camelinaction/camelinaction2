package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cluster.CamelClusterService;
import org.apache.camel.impl.cluster.ClusteredRoutePolicy;

public class FileConsumerRoute extends RouteBuilder {

    private String name;
    private int delay;
    private CamelClusterService cluster;

    public FileConsumerRoute(String name, int delay, CamelClusterService cluster) {
        this.name = name;
        this.delay = delay;
        this.cluster = cluster;
    }

    @Override
    public void configure() throws Exception {
        // read files from the shared directory
        from("file:target/inbox" +
                "?delete=true")
            // setup route policy to be used
            .routePolicy(ClusteredRoutePolicy.forNamespace(cluster, "myNamespace"))
            .log(name + " - Received file: ${file:name}")
            .delay(delay)
            .log(name + " - Done file:     ${file:name}")
            .to("file:target/outbox");
    }

}
