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

import org.apache.camel.Header;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Example how to use JSON data format with the camel-jackson component.
 * <p/>
 * We use camel-jetty to expose a HTTP service which returns the JSON response.
 *
 * @version $Revision$
 */
public class PurchaseOrderJSONTest extends CamelTestSupport {

    private static Logger LOG = LoggerFactory.getLogger(PurchaseOrderJSONTest.class);

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        // register our service bean in the Camel registry
        JndiRegistry jndi = super.createRegistry();
        jndi.bind("orderService", new OrderServiceBean());
        return jndi;
    }

    @Test
    public void testJSON() throws Exception {
        String out = template.requestBody("http://localhost:8080/order/service?id=123", null, String.class);
        LOG.info("Response from order service: " + out);

        assertNotNull(out);
        assertTrue(out.contains("Camel in Action"));
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("jetty://http://0.0.0.0:8080/order/service")
                    .beanRef("orderService", "lookup")
                    .marshal().json(JsonLibrary.Jackson);
            }
        };
    }

    public static class OrderServiceBean {

        public PurchaseOrder lookup(@Header("id") String id) {
            LOG.info("Finding purchase order for id " + id);
            // just return a fixed response
            PurchaseOrder order = new PurchaseOrder();
            order.setPrice(49.95);
            order.setAmount(1);
            order.setName("Camel in Action");
            return order;
        }

    }

}


