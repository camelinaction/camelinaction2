package camelinaction;

import org.apache.camel.main.Main;

public class HelloMain {

    public static void main(String[] args) throws Exception {
        Main main = new Main();

        // bind the bean to the Camel registry using the name hello
        main.bind("hello", new HelloBean());

        // add the route
        main.addRouteBuilder(new HelloRoute());

        // run the application
        main.run();
    }
}
