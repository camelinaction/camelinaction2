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
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Showing how using default error handler to attempt redelivery
 *
 * @version $Revision: 180 $
 */
public class DefaultErrorHandlerTest extends CamelTestSupport {

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        JndiRegistry jndi = super.createRegistry();
        jndi.bind("orderService", new OrderService());
        return jndi;
    }

    @Test
    public void testOrderOk() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:queue.order");
        mock.expectedBodiesReceived("amount=1,name=Camel in Action,id=123,status=OK");

        template.sendBody("seda:queue.inbox","amount=1,name=Camel in Action");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testOrderFail() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:queue.order");
        mock.expectedMessageCount(0);

        template.sendBody("seda:queue.inbox","amount=1,name=ActiveMQ in Action");

        // wait 5 seconds to let this test run as we expect 0 messages
        Thread.sleep(5000);
        
        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // context.setTracing(true);

                errorHandler(defaultErrorHandler()
                    .maximumRedeliveries(2)
                    .redeliveryDelay(1000)
                    .retryAttemptedLogLevel(LoggingLevel.WARN));

                from("seda:queue.inbox")
                    .beanRef("orderService", "validate")
                    .beanRef("orderService", "enrich")
                    .log("Received order ${body}")
                    .to("mock:queue.order");
            }
        };
    }

}
