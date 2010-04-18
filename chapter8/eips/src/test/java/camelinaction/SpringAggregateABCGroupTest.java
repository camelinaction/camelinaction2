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

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Example how to use the groupExchange options.
 * <p/>
 * Please see the Spring XML file for comments
 *
 * @version $Revision$
 */
public class SpringAggregateABCGroupTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/aggregate-abc-group.xml");
    }

    @Test
    public void testABCGroup() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        // one message expected
        mock.expectedMessageCount(1);
        // should not have a body
        mock.message(0).body().isNull();
        // but have it stored in a property as a List
        mock.message(0).property(Exchange.GROUPED_EXCHANGE).isInstanceOf(List.class);

        template.sendBodyAndHeader("direct:start", "A", "myId", 1);
        template.sendBodyAndHeader("direct:start", "B", "myId", 1);
        template.sendBodyAndHeader("direct:start", "F", "myId", 2);
        template.sendBodyAndHeader("direct:start", "C", "myId", 1);

        assertMockEndpointsSatisfied();

        // get the published exchange
        Exchange exchange = mock.getExchanges().get(0);

        // retrieve the List which contains the arrived exchanges
        List list = exchange.getProperty(Exchange.GROUPED_EXCHANGE, List.class);
        assertEquals("Should contain the 3 arrived exchanges", 3, list.size());

        // assert the 3 exchanges are in order and contains the correct body
        Exchange a = (Exchange) list.get(0);
        assertEquals("A", a.getIn().getBody());

        Exchange b = (Exchange) list.get(1);
        assertEquals("B", b.getIn().getBody());

        Exchange c = (Exchange) list.get(2);
        assertEquals("C", c.getIn().getBody());
    }

}