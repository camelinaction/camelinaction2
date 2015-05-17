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
import org.apache.camel.component.hawtdb.HawtDBAggregationRepository;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * The ABC example for using persistent Aggregator EIP.
 * <p/>
 * The test runs for 20 seconds where you can copy files to target/inbox folder.
 * This files are picked up and aggregated. Every 3th file triggers completion.
 * <p/>
 * You can let the test terminate by waiting until it stops. Then start it again
 * so you can continue. For example copy 1 files and wait for the test to stop.
 * Then start the test and copy 2 files, to see that it triggers a completion.
 *
 * @version $Revision$
 */
public class AggregateABCHawtDBTest extends CamelTestSupport {

    @Test
    public void testABCHawtDB() throws Exception {
        System.out.println("Copy 3 files to target/inbox to trigger the completion");
        System.out.println("Files to copy:");
        System.out.println("  copy src/test/resources/a.txt target/inbox");
        System.out.println("  copy src/test/resources/b.txt target/inbox");
        System.out.println("  copy src/test/resources/c.txt target/inbox");
        System.out.println("\nSleeping for 20 seconds");
        System.out.println("You can let the test terminate (or press ctrl +c) and then start it again");
        System.out.println("Which should let you be able to resume.");

        Thread.sleep(20 * 1000);
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                HawtDBAggregationRepository hawtDB = new HawtDBAggregationRepository("myrepo", "data/myrepo.dat");

                from("file:target/inbox")
                    // do a little logging when we load the file
                    .log("Consuming file ${file:name}")
                    // just aggregate all messages
                    .aggregate(constant(true), new MyAggregationStrategy())
                        // use HawtDB as the persistent repository
                        .aggregationRepository(hawtDB)
                        // and complete when we got 3 messages
                        .completionSize(3)
                        // do a little logging for the published message
                        .log("Sending out ${body}")
                        // and send it to the mock
                        .to("mock:result");
            }
        };
    }
}
