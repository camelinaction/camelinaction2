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

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Demonstrates how to use the Load Balancer EIP pattern.
 * <p/>
 * This example sends 4 messages to a Camel route which then sends
 * the message to external services (A and B). We use a failover load balancer
 * in between to send failed messages to the secondary service B in case A failed.
 * <p/>
 * In this example service A and B are in-jvm services using the Camel SEDA
 * queues. This is because this is easy to use for testing.
 * In a real life situation you may balance across external systems such as HTTP/TCP etc.
 *
 * @version $Revision$
 */
public class FailoverLoadBalancerTest extends CamelTestSupport {

    @Test
    public void testLoadBalancer() throws Exception {
        // A should get the 1st, 3rd and 4th message
        MockEndpoint a = getMockEndpoint("mock:a");
        a.expectedBodiesReceived("Hello", "Cool", "Bye");

        // B should get the 2nd
        MockEndpoint b = getMockEndpoint("mock:b");
        b.expectedBodiesReceived("Kaboom");

        // send in 4 messages
        template.sendBody("direct:start", "Hello");
        template.sendBody("direct:start", "Kaboom");
        template.sendBody("direct:start", "Cool");
        template.sendBody("direct:start", "Bye");

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                    // use load balancer with failover strategy
                    .loadBalance().failover()
                        // will send to A first, and if fails then send to B afterwards
                        .to("direct:a").to("direct:b")
                    .end();

                // service A
                from("direct:a")
                    .log("A received: ${body}")
                    // in case of Kaboom the throw an exception to simulate failure
                    .choice()
                        .when(body().contains("Kaboom"))
                            .throwException(new IllegalArgumentException("Damn"))
                        .end()
                    .end()
                    .to("mock:a");

                // service B
                from("direct:b")
                    .log("B received: ${body}")
                    .to("mock:b");
            }
        };
    }

}