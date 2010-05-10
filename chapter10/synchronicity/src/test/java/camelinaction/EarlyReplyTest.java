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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

/**
 * Demonstrates how to return an early reply to a waiting caller
 * while Camel can continue processing the received message afterwards.
 *
 * @version $Revision$
 */
public class EarlyReplyTest extends CamelTestSupport {

    private static final Log LOG = LogFactory.getLog(EarlyReplyTest.class);

    @Test
    public void testEarlyReply() throws Exception {
        final String body = "Hello Camel";

        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedBodiesReceived(body);

        // send an InOut (= requestBody) to Camel
        LOG.info("Caller calling Camel with message: " + body);
        String reply = template.requestBody("http://localhost:8080/early", body, String.class);

        // we should get the reply early which means you should see this log line
        // before Camel has finished processed the message
        LOG.info("Caller finished calling Camel and received reply: " + reply);
        assertEquals("OK", reply);

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("jetty:http://localhost:8080/early")
                    // use wiretap to continue processing the message
                    // in another thread and let this consumer continue
                    .wireTap("direct:incoming")
                    // and return an early reply to the waiting caller
                    .transform().constant("OK");

                from("direct:incoming")
                    // convert the jetty stream to String so we can safely read it multiple times
                    .convertBodyTo(String.class)
                    .log("Incoming ${body}")
                    // simulate processing by delaying 3 seconds
                    .delay(3000)
                    .log("Processing done for ${body}")
                    .to("mock:result");
            }
        };
    }
}
