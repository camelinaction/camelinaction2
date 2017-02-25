package camelinaction;

import org.apache.camel.main.Main;

public class ServerBar {

    private Main main;

    public static void main(String[] args) throws Exception {
        ServerBar bar = new ServerBar();
        bar.boot(args);
    }

    public void boot(String[] args) throws Exception {
        main = new Main();

        // route which uses get/put operations
        main.addRouteBuilder(new CounterRoute("BAR", 9999));
        main.run();
    }

}
