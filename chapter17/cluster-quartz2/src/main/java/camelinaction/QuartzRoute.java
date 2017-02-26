package camelinaction;

import org.apache.camel.builder.RouteBuilder;

/**
 * Route used by both foo and bar server.
 */
public class QuartzRoute extends RouteBuilder {

    private String name;

    public QuartzRoute(String name) {
        this.name = name;
    }

    @Override
    public void configure() throws Exception {
        // run this route once per minute between 08-18 hours on every day the week
        from("quartz2:myGroup/myTrigger?cron=0+0/1+08-18+?+*+*")
            // just log when the job was fired
            .log(name + " running at ${header.fireTime}");
    }

}
