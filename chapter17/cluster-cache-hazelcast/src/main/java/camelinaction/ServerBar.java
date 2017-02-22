package camelinaction;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.apache.camel.main.Main;

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

        main = new Main();
        main.bind("hazelcast", hz);
        main.addRouteBuilder(new CounterRoute("BAR", 9090));
        main.run();
    }

}
