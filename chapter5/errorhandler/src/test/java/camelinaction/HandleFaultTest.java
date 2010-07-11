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

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Showing how to use Camel error handlers to handle faults (SOAP faults)
 *
 * @version $Revision: 181 $
 */
public class HandleFaultTest extends CamelTestSupport {

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        JndiRegistry jndi = super.createRegistry();
        jndi.bind("orderService", new OrderService());
        return jndi;
    }

    @Test
    public void testOrderOk() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:queue.order");
        mock.expectedMessageCount(1);

        template.sendBody("seda:queue.inbox","amount=1,name=Camel in Action");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testOrderFail() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:queue.order");
        mock.expectedMessageCount(0);

        MockEndpoint dead = getMockEndpoint("mock:dead");
        dead.expectedMessageCount(1);
        // and on the EXCEPTION_CAUGHT property we have the caused exception which we can assert contains the fault message
        dead.message(0).property(Exchange.EXCEPTION_CAUGHT).convertTo(String.class).contains("ActiveMQ in Action is out of stock");

        template.sendBody("seda:queue.inbox","amount=1,name=ActiveMQ in Action");

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // we want this Camel error handler to handle SOAP faults
                errorHandler(deadLetterChannel("mock:dead"));

                // enable fault handling globally
                // getContext().setHandleFault(true);

                // or you can enable it specific per route as shown below
                from("seda:queue.inbox").handleFault()
                    .beanRef("orderService", "toSoap")
                    .to("mock:queue.order");
            }
        };
    }

}