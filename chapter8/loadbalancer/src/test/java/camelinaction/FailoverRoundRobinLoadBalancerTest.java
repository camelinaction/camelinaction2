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
 * in round robin mode which then failover to the other service in case of a failure
 *
 * @version $Revision$
 */
public class FailoverRoundRobinLoadBalancerTest extends CamelTestSupport {

    @Test
    public void testLoadBalancer() throws Exception {
        // A should get the 1st and 2nd message
        // Cool will fail at B and then failover to A
        MockEndpoint a = getMockEndpoint("mock:a");
        a.expectedBodiesReceived("Hello", "Boom");

        // B should get the 3rd and 4th
        // Kaboom will fail at A and then failover to B
        MockEndpoint b = getMockEndpoint("mock:b");
        b.expectedBodiesReceived("Bye", "Kaboom");

        // send in 4 messages
        template.sendBody("direct:start", "Hello");
        template.sendBody("direct:start", "Boom");
        template.sendBody("direct:start", "Bye");
        template.sendBody("direct:start", "Kaboom");

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                    // use load balancer with failover strategy
                    // 1 = which will try 1 failover attempt before exhausting
                    // false = do not use Camel error handling
                    // true = use round robin mode
                    .loadBalance().failover(1, false, true)
                        // A and B are two services which is used in round robin fashion
                        .to("direct:a").to("direct:b")
                    .end();

                // service A
                from("direct:a")
                    .log("A received: ${body}")
                    // fail for Kaboom
                    .choice()
                        .when(body().contains("Kaboom"))
                            .throwException(new IllegalArgumentException("Damn"))
                        .end()
                    .end()
                    .to("mock:a");

                // service B
                from("direct:b")
                    .log("B received: ${body}")
                    // fail for Boom
                    .choice()
                        .when(body().contains("Boom"))
                            .throwException(new IllegalArgumentException("Damn"))
                        .end()
                    .end()
                    .to("mock:b");
            }
        };
    }

}
