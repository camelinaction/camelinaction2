package camelinaction;

import org.apache.camel.component.infinispan.InfinispanConfiguration;
import org.apache.camel.component.infinispan.policy.InfinispanRoutePolicy;
import org.apache.camel.main.Main;

public class ServerFoo {

    private Main main;

    public static void main(String[] args) throws Exception {
        ServerFoo foo = new ServerFoo();
        foo.boot();
    }

    public void boot() throws Exception {
        // setup infinispan configuration
        InfinispanConfiguration ic = new InfinispanConfiguration();
        // load infinispan client (hotrod) configuration from the classpath
        ic.setConfigurationUri("hotrod-client.properties");

        // setup the hazelcast route policy
        InfinispanRoutePolicy routePolicy = new InfinispanRoutePolicy(ic);
        // the lock names must be same in the foo and bar server
        routePolicy.setLockMapName("myLock");
        routePolicy.setLockKey("myLockKey");
        routePolicy.setLockValue("myLockValue");

        main = new Main();
        // bind the hazelcast route policy to the name myPolicy which we refer to from the route
        main.bind("myPolicy", routePolicy);
        // add the route and and let the route be named BAR and use a little delay when processing the files
        main.addRouteBuilder(new FileConsumerRoute("FOO", 100));
        main.run();
    }

}
