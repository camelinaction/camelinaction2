package camelinaction;

import org.apache.camel.component.zookeepermaster.policy.MasterRoutePolicy;
import org.apache.camel.main.Main;

public class ServerBar {

    private Main main;

    public static void main(String[] args) throws Exception {
        ServerBar bar = new ServerBar();
        bar.boot();
    }

    public void boot() throws Exception {
        main = new Main();

        // the default zookeeper url is localhost:2181
        MasterRoutePolicy master = new MasterRoutePolicy();
        master.setZooKeeperUrl("localhost:2181");
        master.setGroupName("myGroup");
        main.bind("zookeeper-master-policy", master);

        // add the route and and let the route be named Bar and use a little delay when processing the files
        main.addRouteBuilder(new FileConsumerRoute("Bar", 200));
        main.run();
    }

}
