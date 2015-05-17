package camelinaction;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

public class UseCaseMain {

    public static void main(String[] args) throws Exception {
        UseCaseMain client = new UseCaseMain();
        System.out.println("Starting client... press ctrl + c to stop it");
        client.start();
        System.out.println("... started.");
        System.out.println("Drop files into the target/rider folder");
        System.out.println("for example copy src/main/resources/good.txt to target/rider");
        System.out.println("and copy src/main/resources/bad.txt to target/rider");
        Thread.sleep(99999999);
    }

    private void start() throws Exception {
        CamelContext camel = new DefaultCamelContext();
        camel.addRoutes(new UseCaseRoute());
        camel.start();
    }
}
