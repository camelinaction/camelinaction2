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
import org.apache.camel.Processor;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *  * Demonstrates how to use the Load Balancer EIP pattern.
 *   *
 *    * @version $Revision$
 *     */
public class SpringCircuitBreakerLoadBalancerTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/circuit-breaker-loadbalancer.xml");
    }

    @Test
    public void testLoadBalancer() throws Exception {
        // A should get the 1st, 3rd and 4th message
        MockEndpoint a = getMockEndpoint("mock:a");
        a.expectedBodiesReceived("Got through!");

        // send in 4 messages
        sendMessage("direct:start", "Kaboom");
        sendMessage("direct:start", "Kaboom");
        // circuit should break here as we've had 2 exception occur when accessing remote service
        
        // this call should fail as blocked by circuit breaker
        sendMessage("direct:start", "Blocked");

        // wait so circuit breaker will timeout and go into half-open state
        Thread.sleep(5000);

        // should success
        sendMessage("direct:start", "Got through!");

        assertMockEndpointsSatisfied();
    }

    protected Exchange sendMessage(final String endpoint, final Object body) throws Exception {
        return template.send(endpoint, new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setBody(body);
            }
        });
    }
}
