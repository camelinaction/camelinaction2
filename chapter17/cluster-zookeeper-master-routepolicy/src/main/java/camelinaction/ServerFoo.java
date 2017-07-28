package camelinaction;

import org.apache.camel.component.zookeepermaster.policy.MasterRoutePolicy;
import org.apache.camel.main.Main;

public class ServerFoo {

    private Main main;

    public static void main(String[] args) throws Exception {
        ServerFoo foo = new ServerFoo();
        foo.boot();
    }

    public void boot() throws Exception {
        main = new Main();

        // the default zookeeper url is localhost:2181
        MasterRoutePolicy master = new MasterRoutePolicy();
        master.setZooKeeperUrl("localhost:2181");
        master.setGroupName("myGroup");
        main.bind("zookeeper-master-policy", master);

        // add the route and and let the route be named Bar and use a little delay when processing the files
        main.addRouteBuilder(new FileConsumerRoute("Foo", 200));
        main.run();
    }

}
