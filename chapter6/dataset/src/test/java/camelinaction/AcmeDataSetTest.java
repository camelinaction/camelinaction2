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

import camelinaction.order.acme.InputOrder;
import camelinaction.order.acme.OutputOrder;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.apache.camel.util.ObjectHelper;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Unit test showing how to use a custom data set {@link camelinaction.AcmeDataSet}
 * for testing more complex scenarios where you can construct the messages dynamic
 *
 * @version $Revision$
 */
public class AcmeDataSetTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("camelinaction/camel-dataset.xml");
    }

    /**
     * Just a simple test to show that a single request/reply works
     * (not part of dataset testing)
     */
    @Test
    @Ignore
    public void testSimpleRequestReply() throws Exception {
        InputOrder input = new InputOrder();
        input.setAmount("1");
        input.setCustomerId("123");
        input.setPartId("4444");
        input.setRefNo("9999");

        OutputOrder reply = template.requestBody("cxf:bean:acmeOrderEndpoint", input, OutputOrder.class);
        assertEquals("9999", reply.getCode());
    }

    /**
     * Test using data set
     */
    @Test
    public void testAcmeDataSet() throws Exception {
        // we add a route which contains the dataset test where we route
        // from dataset over CXF and back to dataset
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("dataset:acmeDataSet")
                    .to("cxf:bean:acmeOrderEndpoint")
                    // we must fixup dataset index
                    .process(new RelayDataSetIndex())
                    .to("dataset:acmeDataSet");
            }
        });

        // dataset is a mock so we should just assert that they are satisfied
        assertMockEndpointsSatisfied();
    }

    private class RelayDataSetIndex implements Processor {

        public void process(Exchange exchange) throws Exception {
            // we must extract the dataset index from the CXF reply which is
            // located in the code
            OutputOrder out = exchange.getIn().getBody(OutputOrder.class);

            // extract reference no from the code
            String refNo = ObjectHelper.after(out.getCode(), ";");

            // we use ref no as the index
            exchange.getIn().setHeader(Exchange.DATASET_INDEX, refNo);
        }
    }

}
