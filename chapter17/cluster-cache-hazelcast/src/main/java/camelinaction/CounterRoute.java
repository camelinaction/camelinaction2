package camelinaction;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.hazelcast.HazelcastConstants;

/**
 * Route used by both foo and bar server.
 * <p/>
 * This route will expose a HTTP server on localhost port 8080 or 9090 and then update a counter value
 * on the clustered hazelcast data-grid. The HTTP server returns the current counter value.
 * <p/>
 * The use-case for atomic counter is better implemented as in {@link AtomicCounterRoute} because this
 * implementation is not thread-safe as the foo server may read a value as 16 and then update the value to 17
 * and at the same time the bar server also reads the value as 16 and therefore you can end up with "lost updates".
 * The map is better for storing other kind of shared data in the cluster.
 */
public class CounterRoute extends RouteBuilder {

    private String name;
    private int port;

    public CounterRoute(String name, int port) {
        this.name = name;
        this.port = port;
    }

    @Override
    public void configure() throws Exception {
        // HTTP service
        fromF("jetty:http://localhost:" + port)

            // get the counter from the hazelcast cache
            .setHeader(HazelcastConstants.OBJECT_ID, constant("myCounter"))
            .to("hazelcast:map:myCache?hazelcastInstance=#hz&defaultOperation=get")

            // update the counter using java code
            .process(new Processor() {
                @Override
                public void process(Exchange exchange) throws Exception {
                    Integer counter = exchange.getIn().getBody(Integer.class);
                    if (counter == null) {
                        counter = 0;
                    }
                    counter++;
                    exchange.getIn().setBody(counter);
                }
            })

            // update the counter in the hazelcast cache
            .setHeader(HazelcastConstants.OBJECT_ID, constant("myCounter"))
            .to("hazelcast:map:myCache?hazelcastInstance=#hz&defaultOperation=put")

            // prepare http response
            .log(name + ": counter is now ${body}")
            .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
            .transform().simple("Counter is now ${body}\n");
    }

}
