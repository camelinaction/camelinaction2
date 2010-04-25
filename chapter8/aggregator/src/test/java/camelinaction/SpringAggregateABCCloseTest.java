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

import org.apache.camel.CamelExecutionException;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.processor.aggregate.ClosedCorrelationKeyException;
import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The sample example as {@link AggregateABCCloseTest} but using Spring XML instead.
 * <p/>
 * Please see code comments in the other example.
 *
 * @see MyEndAggregationStrategy
 * @version $Revision$
 */
public class SpringAggregateABCCloseTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/aggregate-abc-close.xml");
    }

    @Test
    public void testABCClose() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        // we expect ABC in the published message
        // notice: Only 1 message is expected
        mock.expectedBodiesReceived("ABC");

        // send the first message
        template.sendBodyAndHeader("direct:start", "A", "myId", 1);
        // send the 2nd message with the same correlation key
        template.sendBodyAndHeader("direct:start", "B", "myId", 1);
        // the F message has another correlation key
        template.sendBodyAndHeader("direct:start", "F", "myId", 2);
        // now we have 3 messages with the same correlation key
        // and the Aggregator should publish the message
        template.sendBodyAndHeader("direct:start", "C", "myId", 1);

        // sending with correlation id 1 should fail as its closed
        try {
            template.sendBodyAndHeader("direct:start", "A2", "myId", 1);
        } catch (CamelExecutionException e) {
            ClosedCorrelationKeyException cause = assertIsInstanceOf(ClosedCorrelationKeyException.class, e.getCause());
            assertEquals("1", cause.getCorrelationKey());
        }

        assertMockEndpointsSatisfied();
    }

}