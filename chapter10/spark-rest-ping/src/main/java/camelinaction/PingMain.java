package camelinaction;

import org.apache.camel.main.Main;

public class PingMain {

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.addRouteBuilder(new PingRoute());

        System.out.println("***************************************************");
        System.out.println("");
        System.out.println("  Ping service");
        System.out.println("");
        System.out.println("  You can try calling this service using:");
        System.out.println("     http://localhost:9090/ping");
        System.out.println("");
        System.out.println("  The Swagger API:");
        System.out.println("     http://localhost:9090/api-doc/swagger.json");
        System.out.println("     http://localhost:9090/api-doc/swagger.yaml");
        System.out.println("***************************************************");

        main.run();
    }
}
