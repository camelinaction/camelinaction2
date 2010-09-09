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
package camelinaction.bindy;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.dataformat.BindyType;
import org.junit.Test;

/**
 * Test that demonstrates how to turn a CSV into a Object using bindy
 *
 * @version $Revision$
 */
public class PurchaseOrderUnmarshalBindyTest extends TestCase {

    @Test
    public void testUnmarshalBindy() throws Exception {
        CamelContext context = new DefaultCamelContext();
        context.addRoutes(createRoute());
        context.start();

        MockEndpoint mock = context.getEndpoint("mock:result", MockEndpoint.class);
        mock.expectedMessageCount(1);

        ProducerTemplate template = context.createProducerTemplate();
        template.sendBody("direct:toObject", "Camel in Action,39.95,1");

        mock.assertIsSatisfied();

        // bindy now turned that into a list of rows so we need to grab the order from the list
        List rows = mock.getReceivedExchanges().get(0).getIn().getBody(List.class);
        // each row is a map with the class name as the index
        Map row = (Map) rows.get(0);
        // get the order from the map
        PurchaseOrder order = (PurchaseOrder) row.get(PurchaseOrder.class.getName());
        assertNotNull(order);

        // assert the order contains the expected data
        assertEquals("Camel in Action", order.getName());
        assertEquals("39.95", order.getPrice().toString());
        assertEquals(1, order.getAmount());
    }

    public RouteBuilder createRoute() {
        return new RouteBuilder() {
            public void configure() throws Exception {
                from("direct:toObject")
                        .unmarshal().bindy(BindyType.Csv, "camelinaction.bindy")
                        .to("mock:result");
            }
        };
    }

}
