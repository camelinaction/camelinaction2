package camelinaction;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.camel.main.Main;

public class ServerFoo {

    private Main main;

    public static void main(String[] args) throws Exception {
        ServerFoo foo = new ServerFoo();
        foo.boot(args);
    }

    public void boot(String[] args) throws Exception {
        main = new Main();

        // load infinispan client properties
        Properties client = new Properties();
        client.load(new FileInputStream("src/main/resources/hotrod-client.properties"));

        // and bind in Camel registry so we can use it by the jcache component
        main.bind("hotrod", client);

        main.addRouteBuilder(new CounterRoute("FOO", 8888));
        main.run();
    }

}
