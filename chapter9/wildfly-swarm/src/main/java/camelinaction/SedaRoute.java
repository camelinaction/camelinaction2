package camelinaction;

import javax.inject.Singleton;

import org.apache.camel.builder.RouteBuilder;

/**
 * Routing between two internal SEDA queues in Camel
 */
@Singleton
public class SedaRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("seda:inbox")
            .to("seda:outbox");
    }

}
