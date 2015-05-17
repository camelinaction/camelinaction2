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
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * A simple example showing a async caller sending an InOnly message to Camel.
 * The message is being routed in Camel using multiple threads. However this
 * wont impact the caller as its async.
 *
 * @version $Revision$
 */
public class AsyncMultipleThreadsTest extends CamelTestSupport {

    @Test
    public void testAsyncInOnly() throws Exception {
        String body = "Hello Camel";

        // send an InOnly (= sendBody) to Camel
        log.info("Caller calling Camel with message: " + body);
        template.sendBody("seda:start", body);
        log.info("Caller finished calling Camel");

        // give time for route to complete
        Thread.sleep(1000);
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // route the message to a log so we can see details about MEP and thread name
                from("seda:start")
                    .to("log:A")
                    // cause this route to use multiple threads
                    .threads(5, 10)
                    .to("log:B");
            }
        };
    }
}
