package camelinaction;

import org.apache.camel.component.consul.policy.ConsulRoutePolicy;
import org.apache.camel.main.Main;

public class ServerFoo {

    private Main main;

    public static void main(String[] args) throws Exception {
        ServerFoo foo = new ServerFoo();
        foo.boot();
    }

    public void boot() throws Exception {
        // setup the hazelcast route policy
        ConsulRoutePolicy routePolicy = new ConsulRoutePolicy();
        // the service names must be same in the foo and bar server
        routePolicy.setServiceName("myLock");
        routePolicy.setTtl(5);

        main = new Main();
        // bind the hazelcast route policy to the name myPolicy which we refer to from the route
        main.bind("myPolicy", routePolicy);
        // add the route and and let the route be named Bar and use a little delay when processing the files
        main.addRouteBuilder(new FileConsumerRoute("Foo", 100));
        main.run();
    }

}
