package camelinaction;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.apache.camel.main.Main;

public class ServerFoo {

    private Main main;

    public static void main(String[] args) throws Exception {
        ServerFoo foo = new ServerFoo();
        foo.boot(args);
    }

    public void boot(String[] args) throws Exception {
        // create and embed the hazelcast server
        // (you can use the hazelcast client if you want to connect to external hazelcast server)
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();

        main = new Main();
        main.bind("hz", hz);

        if (args.length == 0) {
            // route which uses get/put operations
            main.addRouteBuilder(new CounterRoute("Foo", 8080));
        } else {
            // route which uses atomic counter
            main.addRouteBuilder(new AtomicCounterRoute("Foo", 8080));
        }
        main.run();
    }

}
