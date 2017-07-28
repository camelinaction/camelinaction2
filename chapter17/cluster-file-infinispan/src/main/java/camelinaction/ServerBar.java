package camelinaction;

import java.util.Properties;

import org.apache.camel.component.infinispan.InfinispanConfiguration;
import org.apache.camel.component.infinispan.policy.InfinispanRoutePolicy;
import org.apache.camel.main.Main;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;

public class ServerBar {

    private Main main;

    public static void main(String[] args) throws Exception {
        ServerBar bar = new ServerBar();
        bar.boot();
    }

    public void boot() throws Exception {
        Properties props = new Properties();
        // list of urls for the infinispan server
        // as we run in domain node we have two servers out of the box, and can therefore include both
        // that the client can load balance/failover to be highly available
        props.setProperty("infinispan.client.hotrod.server_list", "localhost:11222;localhost:11372");
        // by default, previously existing values for java.util.Map operations
        // are not returned for remote caches but they are required for the route
        // policy to work.
        props.setProperty("infinispan.client.hotrod.force_return_values", "true");

        // create remote infinispan cache manager and start it
        RemoteCacheManager remote = new RemoteCacheManager(
            new ConfigurationBuilder().withProperties(props).build(),
            true
        );

        // setup Camel infinispan configuration to use the remote cache manager
        InfinispanConfiguration ic = new InfinispanConfiguration();
        ic.setCacheContainer(remote);

        // setup the hazelcast route policy
        InfinispanRoutePolicy routePolicy = new InfinispanRoutePolicy(ic);
        // the lock names must be same in the foo and bar server
        routePolicy.setLockMapName("myLock");
        routePolicy.setLockKey("myLockKey");
        // the lock value identifies the node
        routePolicy.setLockValue("bar");

        main = new Main();
        // bind the hazelcast route policy to the name myPolicy which we refer to from the route
        main.bind("myPolicy", routePolicy);
        // add the route and and let the route be named Bar and use a little delay when processing the files
        main.addRouteBuilder(new FileConsumerRoute("Bar", 100));
        main.run();
    }

}
