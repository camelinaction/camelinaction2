package camelinaction;

import org.apache.camel.builder.RouteBuilder;

/**
 * This route has the service call and all its configuration embedded in the route itself
 */
public class MyStaticRouteEmbedded extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("timer:trigger?period=2000")
            .serviceCall()
                // use the http4 component to call the service
                .component("http4")
                // `hello-service` = name of service
                // `/camel/hello` is used in uri templating which
                // means this is used in the context-path of the actual http4 uri
                .name("hello-service/camel/hello")
                // add the static list of servers
                .staticServiceDiscovery()
                    // the syntax is name@hostname:port
                    // and you can separate multiple servers by comma
                    .servers("hello-service@localhost:8081,hello-service@localhost:8082")
                .end() // end static list
            .end() // end service call
            .log("Response ${body}");
    }
}
