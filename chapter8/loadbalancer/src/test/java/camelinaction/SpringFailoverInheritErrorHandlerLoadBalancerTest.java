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
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Demonstrates how to use the Load Balancer EIP pattern.
 *
 * @version $Revision$
 */
public class SpringFailoverInheritErrorHandlerLoadBalancerTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/failover-inheriterrorhandler-loadbalancer.xml");
    }

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

}
