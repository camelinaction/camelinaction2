package camelinaction;

import org.apache.camel.main.Main;

public class ServerFoo {

    private Main main;

    public static void main(String[] args) throws Exception {
        ServerFoo foo = new ServerFoo();
        foo.boot(args);
    }

    public void boot(String[] args) throws Exception {
        main = new Main();
        main.addRouteBuilder(new CounterRoute("FOO", 8888));
        main.run();
    }

}
