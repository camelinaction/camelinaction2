package camelinaction;

import org.apache.camel.component.consul.cluster.ConsulClusterService;
import org.apache.camel.main.Main;

public class ServerBar {

    private Main main;

    public static void main(String[] args) throws Exception {
        ServerBar bar = new ServerBar();
        bar.boot();
    }

    public void boot() throws Exception {
        // setup the consul cluster service
        ConsulClusterService cluster = new ConsulClusterService();
        // the service names must be same in the foo and bar server
        cluster.setId("bar");
        cluster.setTtl(5);

        main = new Main();
        // add the route and and let the route be named Bar and use a little delay when processing the files
        main.addRouteBuilder(new FileConsumerRoute("Bar", 100, cluster));
        main.run();
    }

}
