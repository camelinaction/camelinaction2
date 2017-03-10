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

        // url to consume files from
        String url = "file:target/inbox?delete=true";

        // use the zookeeper master component in the clustered group named myGroup
        // to run a master/slave mode in the following Camel url
        from("zookeeper-master:myGroup:" + url)
            .log(name + " - Received file: ${file:name}")
            .delay(delay)
            .log(name + " - Done file:     ${file:name}")
            .to("file:target/outbox");
    }

}
