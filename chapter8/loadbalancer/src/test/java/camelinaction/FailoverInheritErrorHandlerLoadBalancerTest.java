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

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Demonstrates how to use the Load Balancer EIP pattern.
 * <p/>
 * This example sends 2 messages to a Camel route which then sends
 * the message to external services (A and B). We use a failover load balancer
 * in between to send failed messages to the secondary service B in case A failed.
 * <p/>
 * In this example we also let the Camel error handler play a role as it will handle
 * the failure first, and only when it gives up we let the failover load balancer
 * react and attempt failover.
 *
 * @version $Revision$
 */
public class FailoverInheritErrorHandlerLoadBalancerTest extends CamelTestSupport {

    @Test
    public void testLoadBalancer() throws Exception {
        // simulate error when sending to service A
        context.getRouteDefinition("start").adviceWith(context, new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                interceptSendToEndpoint("direct:a")
                    .choice()
                        .when(body().contains("Kaboom"))
                            .throwException(new IllegalArgumentException("Damn"))
                        .end()
                    .end();
            }
        });

        // A should get the 1st
        MockEndpoint a = getMockEndpoint("mock:a");
        a.expectedBodiesReceived("Hello");

        // B should get the 2nd
        MockEndpoint b = getMockEndpoint("mock:b");
        b.expectedBodiesReceived("Kaboom");

        // send in 2 messages
        template.sendBody("direct:start", "Hello");
        template.sendBody("direct:start", "Kaboom");

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // configure error handler to try at most 3 times with 2 sec delay
                errorHandler(defaultErrorHandler()
                    .maximumRedeliveries(3).redeliveryDelay(2000)
                    // reduce some logging noise
                    .retryAttemptedLogLevel(LoggingLevel.WARN)
                    .retriesExhaustedLogLevel(LoggingLevel.WARN)
                    .logStackTrace(false));

                from("direct:start").routeId("start")
                    // use load balancer with failover strategy
                    // 1 = which will try 1 failover attempt before exhausting
                    // true = do use Camel error handling
                    // false = do not use round robin mode
                    .loadBalance().failover(1, true, false)
                        // will send to A first, and if fails then send to B afterwards
                        .to("direct:a").to("direct:b")
                    .end();

                // service A
                from("direct:a")
                    .log("A received: ${body}")
                    .to("mock:a");

                // service B
                from("direct:b")
                    .log("B received: ${body}")
                    .to("mock:b");
            }
        };
    }

}
