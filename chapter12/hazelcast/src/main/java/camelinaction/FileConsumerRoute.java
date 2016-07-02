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
                "?delete=true" +
                "&readLock=idempotent" +                   // use idempotent read lock
                "&idempotentRepository=#myRepo" +          // refer to the idempotent repository
                "&readLockLoggingLevel=WARN" +             // logging level, you can set this to DEBUG/OFF for production
                "&shuffle=true")                           // sort the files by random to reduce the chance of multiple nodes trying to process the same file
            .log(name + " - Received file: ${file:name}")
            .delay(delay)
            .log(name + " - Done file:     ${file:name}")
            .to("file:target/outbox");
    }

}
