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

import java.util.concurrent.TimeUnit;

import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Processing a big with concurrency using the parallelProcessing option.
 *
 * @version $Revision$
 */
public class BigFileParallelTest extends CamelTestSupport {

    @Test
    public void testBigFile() throws Exception {
        NotifyBuilder notify = new NotifyBuilder(context).whenDone(1).create();

        long start = System.currentTimeMillis();

        System.out.println("Waiting to be done with 2 min timeout (use ctrl + c to stop)");
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
                    // by enabling the parallel processing the output from the split EIP will
                    // be processed concurrently using the default thread pool settings
                    // which is a grow/shrink pool between 10-20 threads
                    // inactive threads will terminate after 60 seconds
                    .split(body().tokenize("\n")).streaming().parallelProcessing()
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