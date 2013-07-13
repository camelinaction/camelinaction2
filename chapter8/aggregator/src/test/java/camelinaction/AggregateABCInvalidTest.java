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
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * The ABC example for using the Aggregator EIP.
 * <p/>
 * This example have 4 messages send to the aggregator, by which one
 * message is published which contains the aggregation of message 1,2 and 4
 * as they use the same correlation key.
 * <p/>
 * And this time we ignore invalid correlation keys which avoids Camel thrown an exception
 * if the correlation key is missing
 * <p/>
 * See the class {@link camelinaction.MyAggregationStrategy} for how the messages
 * are actually aggregated together.
 *
 * @see camelinaction.MyAggregationStrategy
 * @version $Revision$
 */
public class AggregateABCInvalidTest extends CamelTestSupport {

    @Test
    public void testABCInvalid() throws Exception {
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
        // this message has no correlation key so its ignored
        template.sendBody("direct:start", "C");
        // so we resend the message this time with a correlation key
        template.sendBodyAndHeader("direct:start", "C", "myId", 1);

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                    // do a little logging
                    .log("Sending ${body} with correlation key ${header.myId}")
                    // aggregate based on header correlation key
                    // use class MyAggregationStrategy for aggregation
                    // and complete when we have aggregated 3 messages
                    .aggregate(header("myId"), new MyAggregationStrategy()).completionSize(3)
                        // and ignore invalid correlation keys
                        .ignoreInvalidCorrelationKeys()
                        // do a little logging for the published message
                        .log("Sending out ${body}")
                        // and send it to the mock
                        .to("mock:result");
            }
        };
    }
}
