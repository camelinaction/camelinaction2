package camelinaction;

import org.apache.camel.component.quartz2.QuartzComponent;
import org.apache.camel.main.Main;

public class ServerBar {

    private Main main;

    public static void main(String[] args) throws Exception {
        ServerBar bar = new ServerBar();
        bar.boot();
    }

    public void boot() throws Exception {
        main = new Main();

        // setup quartz component
        QuartzComponent quartz = new QuartzComponent();
        quartz.setPropertiesFile("quartz.properties");

        // add the component to Camel
        main.bind("quartz2", quartz);

        // route which uses get/put operations
        main.addRouteBuilder(new QuartzRoute("Bar"));
        main.run();
    }

}
