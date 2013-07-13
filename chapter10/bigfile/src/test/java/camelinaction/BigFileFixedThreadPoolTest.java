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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Processing a big with concurrency using a custom thread pool created by the JDK.
 *
 * @version $Revision$
 */
public class BigFileFixedThreadPoolTest extends CamelTestSupport {

    private ExecutorService threadPool;

    @Before
    @Override
    public void setUp() throws Exception {
        // must create the custom thread pool before Camel is starting
        threadPool = Executors.newFixedThreadPool(20);
        super.setUp();
    }

    @After
    @Override
    public void tearDown() throws Exception {
        // proper cleanup by shutting down the thread pool
        threadPool.shutdownNow();
        super.tearDown();
    }

    @Test
    public void testBigFile() throws Exception {
        // when the first exchange is done
        NotifyBuilder notify = new NotifyBuilder(context).whenDoneByIndex(0).create();

        long start = System.currentTimeMillis();

        System.out.println("Waiting to be done with 5 min timeout (use ctrl + c to stop)");
        notify.matches(2 * 60, TimeUnit.SECONDS);

        long delta = System.currentTimeMillis() - start;
        System.out.println("Took " + delta / 1000 + " seconds");
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("file:target/inventory?noop=true")
                    .log("Starting to process big file: ${header.CamelFileName}")
                    // use a custom thread pool so the output from the split EIP will
                    // be processed concurrently
                    .split(body().tokenize("\n")).streaming().executorService(threadPool)
                        .bean(InventoryService.class, "csvToObject")
                        .to("direct:update")
                    .end()
                    .log("Done processing big file: ${header.CamelFileName}");

                from("direct:update")
                    .bean(InventoryService.class, "updateInventory");
            }
        };
    }
}
