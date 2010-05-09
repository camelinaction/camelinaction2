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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.SynchronizationAdapter;
import org.apache.camel.spi.Synchronization;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

/**
 * Demonstrating how to use callback to gather a number of concurrent replies within a
 * given time period.
 *
 * @version $Revision$
 */
public class RiderAutoPartsCallbackTest extends CamelTestSupport {

    private static Log LOG = LogFactory.getLog(RiderAutoPartsCallbackTest.class);
    private int numPartners = 5;

    @Test
    public void testCallback() throws Exception {
        // related is the list of related items
        final List<String> relates = new ArrayList<String>();

        // latch to count down every time we got a reply
        final CountDownLatch latch = new CountDownLatch(numPartners);

        // use this callback to gather the replies and add it to the related list
        Synchronization callback = new SynchronizationAdapter() {
            @Override
            public void onComplete(Exchange exchange) {
                // get the reply and add it to related
                relates.add(exchange.getOut().getBody(String.class));
            }

            @Override
            public void onDone(Exchange exchange) {
                // count down the latch
                latch.countDown();
            }
        };

        // send the same message to the business partners so they can return their feedback
        String body = "bumper";
        for (int i = 0; i < numPartners; i++) {
            template.asyncCallbackRequestBody("seda:partner:" + i, body, callback);
        }
        LOG.info("Send " + numPartners + " messages to partners.");

        // wait at most 1.5 seconds or until we got all replies
        boolean all = latch.await(1500, TimeUnit.MILLISECONDS);

        // log what we got as reply
        LOG.info("Got " + relates.size() + " replies, is all? " + all);
        for (String related : relates) {
            LOG.info("Related item category is: " + related);
        }

        // assert the unit test
        assertEquals(3, relates.size());
        assertEquals("bumper extension", relates.get(0));
        assertEquals("bumper filter", relates.get(1));
        assertEquals("bumper cover", relates.get(2));
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // business partner routes with different delays
                // so some will answer within the period and others wont

                from("seda:partner:0").delay(500).transform().simple("bumper filter");

                from("seda:partner:1").delay(3000).transform().simple("nose panel");

                from("seda:partner:2").delay(1000).transform().simple("bumper cover");

                from("seda:partner:3").delay(250).transform().simple("bumper extension");

                from("seda:partner:4").delay(2000).transform().simple("tow hooks");
            }
        };
    }
}
