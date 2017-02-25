package camelinaction;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.camel.main.Main;

public class ServerBar {

    private Main main;

    public static void main(String[] args) throws Exception {
        ServerBar bar = new ServerBar();
        bar.boot(args);
    }

    public void boot(String[] args) throws Exception {
        main = new Main();

        // load infinispan client properties
        Properties client = new Properties();
        client.load(new FileInputStream("src/main/resources/hotrod-client.properties"));

        // and bind in Camel registry so we can use it by the jcache component
        main.bind("hotrod", client);

        // route which uses get/put operations
        main.addRouteBuilder(new CounterRoute("BAR", 8889));
        main.run();
    }

}
