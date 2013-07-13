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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.SynchronizationAdapter;
import org.apache.camel.spi.Synchronization;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * A test which shows how to use the Camel callback mechanism to let the callback
 * handle the replies when they are returned.
 *
 * @version $Revision$
 */
public class CamelCallbackTest extends CamelTestSupport {

    @Test
    public void testCamelCallback() throws Exception {
        // echos is the list of replies
        final List<String> echos = new ArrayList<String>();
        final CountDownLatch latch = new CountDownLatch(3);

        // use this callback to gather the replies and add it to the echos list
        Synchronization callback = new SynchronizationAdapter() {
            @Override
            public void onDone(Exchange exchange) {
                // get the reply and add it to echoes
                echos.add(exchange.getOut().getBody(String.class));
                // count down latch when we receive a response
                latch.countDown();
            }
        };

        // now submit 3 async request/reply messages and use the same callback to
        // handle the replies
        template.asyncCallbackRequestBody("seda:echo", "Donkey", callback);
        template.asyncCallbackRequestBody("seda:echo", "Tiger", callback);
        template.asyncCallbackRequestBody("seda:echo", "Camel", callback);

        // wait until the messages is done, or timeout after 6 seconds
        latch.await(6, TimeUnit.SECONDS);

        // assert we got 3 replies
        assertEquals(3, echos.size());
        // sort list so we can assert by index
        Collections.sort(echos);
        assertEquals("CamelCamel", echos.get(0));
        assertEquals("DonkeyDonkey", echos.get(1));
        assertEquals("TigerTiger", echos.get(2));
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // this is the route we want to send messages to
                // in an async manner.
                // usually the route is something that takes some time to do
                from("seda:echo?concurrentConsumers=5")
                    .log("Starting to route ${body}")
                    .delay(3000)
                    .transform().simple("${body}${body}")
                    .log("Route is now done");
            }
        };
    }
}
