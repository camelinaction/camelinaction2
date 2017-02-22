package camelinaction;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.hazelcast.HazelcastConstants;

public class CounterRoute extends RouteBuilder {

    private String name;
    private int port;

    public CounterRoute(String name, int port) {
        this.name = name;
        this.port = port;
    }

    @Override
    public void configure() throws Exception {
        // read files from the shared directory
        fromF("jetty:http://localhost:" + port)

            // get the counter from the hazelcast cache
            .setHeader(HazelcastConstants.OBJECT_ID, constant("myCounter"))
            .to("hazelcast:map:myCache?hazelcastInstance=#hazelcast&defaultOperation=get")

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
            .to("hazelcast:map:myCache?hazelcastInstance=#hazelcast&defaultOperation=put")

            // prepare http response
            .log(name + ": counter is now ${body}")
            .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
            .transform().simple("Counter is now ${body}\n");
    }

}
