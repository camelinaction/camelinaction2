package camelinaction;

import org.apache.camel.component.consul.cluster.ConsulClusterService;
import org.apache.camel.main.Main;

public class ServerFoo {

    private Main main;

    public static void main(String[] args) throws Exception {
        ServerFoo foo = new ServerFoo();
        foo.boot();
    }

    public void boot() throws Exception {
        // setup the consul cluster service
        ConsulClusterService cluster = new ConsulClusterService();
        // the service names must be same in the foo and bar server
        cluster.setId("foo");
        cluster.setTtl(5);

        main = new Main();
        // add the route and and let the route be named Bar and use a little delay when processing the files
        main.configure().addRoutesBuilder(new FileConsumerRoute("Foo", 100, cluster));
        main.run();
    }

}
