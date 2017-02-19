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
        from("file:target/inbox" +
                "?delete=true")
            // setup route policy to be used
            .routePolicyRef("myPolicy")
            .log(name + " - Received file: ${file:name}")
            .delay(delay)
            .log(name + " - Done file:     ${file:name}")
            .to("file:target/outbox");
    }

}
