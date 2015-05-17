package camelinaction;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class HttpServer {

    private static String url = "http://localhost:8765/rider";

    public static void main(String[] args) throws Exception {
        HttpServer server = new HttpServer();
        System.out.println("Starting HttpServer... press ctrl + c to stop it");
        server.server();
        System.out.println("... started and listening on: " + url);
        Thread.sleep(999999999);
    }

    public void server() throws Exception {
        CamelContext camel = new DefaultCamelContext();
        camel.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("jetty:" + url)
                    .process(new Processor() {
                        public void process(Exchange exchange) throws Exception {
                            String body = exchange.getIn().getBody(String.class);

                            System.out.println("Received message: " + body);

                            if (body != null && body.contains("Kabom")) {
                                throw new Exception("ILLEGAL DATA");
                            }
                            exchange.getOut().setBody("OK");
                        }
                    });
            }
        });
        camel.start();

    }
}
