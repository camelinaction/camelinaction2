package camelinaction;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.apache.camel.component.hazelcast.policy.HazelcastRoutePolicy;
import org.apache.camel.main.Main;
import org.apache.camel.processor.idempotent.hazelcast.HazelcastIdempotentRepository;

public class ServerFoo {

    private Main main;

    public static void main(String[] args) throws Exception {
        ServerFoo foo = new ServerFoo();
        foo.boot();
    }

    public void boot() throws Exception {
        // create and embed the hazelcast server
        // (you can use the hazelcast client if you want to connect to external hazelcast server)
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();

        // setup the hazelcast route policy
        HazelcastRoutePolicy routePolicy = new HazelcastRoutePolicy(hz);

        main = new Main();
        // bind the hazelcast route policy to the name myPolicy which we refer to from the route
        main.bind("myPolicy", routePolicy);
        // add the route and and let the route be named BAR and use a little delay when processing the files
        main.addRouteBuilder(new FileConsumerRoute("FOO", 100));
        main.run();
    }

}
