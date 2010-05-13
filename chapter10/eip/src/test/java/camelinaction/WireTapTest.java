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

import java.util.concurrent.ExecutorService;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.ThreadPoolBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Demonstrates how to use a custom thread pool with the WireTap EIP pattern.
 *
 * @version $Revision$
 */
public class WireTapTest extends CamelTestSupport {

    @Test
    public void testWireTap() throws Exception {
        getMockEndpoint("mock:result").expectedBodiesReceived("Hello Camel");
        getMockEndpoint("mock:tap").expectedBodiesReceived("Hello Camel");

        template.sendBody("direct:start", "Hello Camel");

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // create a custom thread pool
                ExecutorService lowPool = new ThreadPoolBuilder(context)
                        .poolSize(1).maxPoolSize(5).build("LowPool");

                // which we want the WireTap to use
                from("direct:start")
                    .log("Incoming message ${body}")
                    .wireTap("direct:tap", lowPool)
                    .to("mock:result");

                from("direct:tap")
                    .log("Tapped message ${body}")
                    .to("mock:tap");
            }
        };
    }
}
