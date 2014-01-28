/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package camelinaction;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * @version $Revision$
 */
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
