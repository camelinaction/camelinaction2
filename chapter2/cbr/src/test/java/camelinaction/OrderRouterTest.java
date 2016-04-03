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

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class OrderRouterTest extends CamelTestSupport {

    @Override
    protected CamelContext createCamelContext() throws Exception {
        // create CamelContext
        CamelContext camelContext = super.createCamelContext();
        
        // connect to embedded ActiveMQ JMS broker
        ConnectionFactory connectionFactory = 
            new ActiveMQConnectionFactory("vm://localhost");
        camelContext.addComponent("jms",
            JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
        
        return camelContext;
    }
    
    @Test
    public void testPrintFile() throws Exception {
        getMockEndpoint("mock:xml").expectedMessageCount(1);
        getMockEndpoint("mock:csv").expectedMessageCount(1);

        assertMockEndpointsSatisfied();
    }
    
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // load file orders from src/data into the JMS queue
                from("file:src/data?noop=true").to("jms:incomingOrders");
        
                // content-based router
                from("jms:incomingOrders")
                .choice()
                    .when(header("CamelFileName").endsWith(".xml"))
                        .to("jms:xmlOrders")  
                    .when(header("CamelFileName").endsWith(".csv"))
                        .to("jms:csvOrders");
                
                // test that our route is working
                from("jms:xmlOrders").process(new Processor() {
                    public void process(Exchange exchange) throws Exception {
                        System.out.println("Received XML order: " 
                                + exchange.getIn().getHeader("CamelFileName"));   
                    }
                })
                .to("mock:xml");                
                
                from("jms:csvOrders").process(new Processor() {
                    public void process(Exchange exchange) throws Exception {
                        System.out.println("Received CSV order: " 
                                + exchange.getIn().getHeader("CamelFileName"));   
                    }
                })
                .to("mock:csv");
            }
        };
    }
}
