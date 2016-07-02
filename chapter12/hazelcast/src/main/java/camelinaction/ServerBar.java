package camelinaction;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.apache.camel.main.Main;
import org.apache.camel.processor.idempotent.hazelcast.HazelcastIdempotentRepository;

public class ServerBar {

    private Main main;

    public static void main(String[] args) throws Exception {
        ServerBar bar = new ServerBar();
        bar.boot();
    }

    public void boot() throws Exception {
        // create and embed the hazelcast server
        // (you can use the hazelcast client if you want to connect to external hazelcast server)
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();

        // setup the hazelcast idempotent repository which we will use in the route
        HazelcastIdempotentRepository repo = new HazelcastIdempotentRepository(hz, "camel");

        main = new Main();
        // bind the hazelcast repository to the name myRepo which we refer to from the route
        main.bind("myRepo", repo);
        // add the route and and let the route be named BAR and use a little delay when processing the files
        main.addRouteBuilder(new FileConsumerRoute("BAR", 100));
        main.run();
    }

}
