package camelinaction;

import org.apache.camel.main.Main;

public class MetricsMain {

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.configure().addRoutesBuilder(new MetricsRouteBuilder());
        main.run();
    }
}
