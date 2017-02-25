package camelinaction;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.hazelcast.HazelcastConstants;

/**
 * Route used by both foo and bar server.
 * <p/>
 * This route will expose a HTTP server on localhost port 8080 or 9090 and then increment an atomic counter
 * on the clustered hazelcast data-grid. The HTTP server returns the current counter value.
 */
public class AtomicCounterRoute extends RouteBuilder {

    private String name;
    private int port;

    public AtomicCounterRoute(String name, int port) {
        this.name = name;
        this.port = port;
    }

    @Override
    public void configure() throws Exception {
        // HTTP service
        fromF("jetty:http://localhost:" + port)

            // increase the atomic clustered counter from the hazelcast cache
            .setHeader(HazelcastConstants.OBJECT_ID, constant("myCounter"))
            .to("hazelcast:atomicvalue:Cache?hazelcastInstance=#hz&defaultOperation=increment")

            // prepare http response
            .log(name + ": counter is now ${body}")
            .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
            .transform().simple("Atomic Counter is now ${body}\n");
    }

}
