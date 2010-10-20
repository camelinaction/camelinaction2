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

import java.util.concurrent.Future;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

/**
 * Demonstrates how the client concurrency API in Camel works
 * for sending messages in an asynchronous manner.
 *
 * @version $Revision$
 */
public class CamelFutureDoneTest extends CamelTestSupport {

    private static Log LOG = LogFactory.getLog(CamelFutureDoneTest.class);

    @Test
    public void testFutureDone() throws Exception {
        // now send the message to the endpoint in async manner
        // and get the Future handle back so we can later get the result
        LOG.info("Submitting task to Camel");
        Future<String> future = template.asyncRequestBody("seda:quote", "Hello Camel", String.class);
        LOG.info("Task submitted and we got a Future handle");

        // test when we are done
        boolean done = false;
        while (!done) {
            done = future.isDone();
            LOG.info("Is the task done? " + done);
            if (!done) {
                Thread.sleep(2000);
            }
        }

        // and get the answer
        String answer = future.get();
        LOG.info("The answer is: " + answer);
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // this is the route we want to send messages to
                // in an async manner.
                // usually the route is something that takes some time to do
                from("seda:quote")
                    .log("Starting to route ${body}")
                    .delay(5000)
                    .transform().constant("Camel rocks")
                    .log("Route is now done");
            }
        };
    }
}
