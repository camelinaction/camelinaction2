package camelinaction;

import org.apache.camel.main.Main;

public class MetricsMain {

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.addRouteBuilder(new MetricsRouteBuilder());
        main.enableHangupSupport();
        main.run();
    }
}
