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
import org.apache.camel.component.dataset.DataSet;
import org.apache.camel.component.dataset.SimpleDataSet;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Unit test showing how to use the SimpleDataSet to get started with DataSet.
 * 
 * @version $Revision$
 */
public class SimpleDataSetTest extends CamelTestSupport {

    @Test
    public void testSimpleDataSet() throws Exception {
        // start and verify that the test passed
        assertMockEndpointsSatisfied();
    }

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        // use the simple dataset with 1000 messages
        DataSet simple = new SimpleDataSet(1000);

        // register the dataset in the Registry
        // which is used by the DataSet endpoint
        JndiRegistry jndi = super.createRegistry();
        jndi.bind("quotes", simple);
        return jndi;
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // start the test from this dataset
                from("dataset:quotes")
                    .to("seda:queue.quotes");

                // this is the route we want to test
                from("seda:queue.quotes")
                    .choice()
                        .when(body().contains("Camel"))
                            .to("seda:queue.quotes.camel")
                        .otherwise()
                            .to("seda:queue.quotes.other")
                    .end()
                    // at the end of this route make sure to route to the same
                    // dataset endpoint so it knows which messages have arrived etc.
                    .to("dataset:quotes");
            }
        };
    }

}
