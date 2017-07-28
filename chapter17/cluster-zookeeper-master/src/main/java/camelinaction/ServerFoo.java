package camelinaction;

import org.apache.camel.component.zookeepermaster.MasterComponent;
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
        MasterComponent master = new MasterComponent();
        master.setZooKeeperUrl("localhost:2181");
        main.bind("zookeeper-master", master);

        // add the route and and let the route be named Bar and use a little delay when processing the files
        main.addRouteBuilder(new FileConsumerRoute("Foo", 200));
        main.run();
    }

}
