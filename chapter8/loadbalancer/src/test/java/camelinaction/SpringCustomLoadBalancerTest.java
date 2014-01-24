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

import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Demonstrates how to use the Load Balancer EIP pattern.
 * <p/>
 * In this example we use a custom load balancer {@link camelinaction.MyCustomLoadBalancer} which dictates
 * how messages is being balanced at runtime.
 *
 * @version $Revision$
 */
public class SpringCustomLoadBalancerTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/custom-loadbalancer.xml");
    }

    @Test
    public void testLoadBalancer() throws Exception {
        // A should get the gold messages
        MockEndpoint a = getMockEndpoint("mock:a");
        a.expectedBodiesReceived("Camel rocks", "Cool");

        // B should get the other messages
        MockEndpoint b = getMockEndpoint("mock:b");
        b.expectedBodiesReceived("Hello", "Bye");

        // send in 4 messages
        template.sendBodyAndHeader("direct:start", "Hello", "type", "silver");
        template.sendBodyAndHeader("direct:start", "Camel rocks", "type", "gold");
        template.sendBodyAndHeader("direct:start", "Cool", "type", "gold");
        template.sendBodyAndHeader("direct:start", "Bye", "type", "bronze");

        assertMockEndpointsSatisfied();
    }
}
