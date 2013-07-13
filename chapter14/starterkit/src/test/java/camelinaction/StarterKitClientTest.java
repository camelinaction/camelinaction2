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

import java.util.List;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Unit test to demonstrate the principle of using Camel Proxy to hide the middleware.
 * <p/>
 * Notice the unit test methods does not use any Camel ProducerTemplate to send messages
 * to Camel. They use a clean interface, the RiderService interface.
 *
 * @version $Revision$
 */
public class StarterKitClientTest extends CamelSpringTestSupport {

    private static final Log LOG = LogFactory.getLog(StarterKitClientTest.class);

    protected int getExpectedRouteCount() {
        // we do not have any routes (no routes in camel-client.xml)
        return 0;
    }

    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/camel-server.xml", "META-INF/spring/camel-client.xml");
    }

    @Test
    public void testStarterKitUpdateInventory() throws Exception {
        // get the proxied interface for the client to use
        RiderService rider = context.getRegistry().lookupByNameAndType("rider", RiderService.class);

        // prepare an inventory update to be send
        Inventory inventory = new Inventory("1234", "4444");
        inventory.setName("Bumper");
        inventory.setAmount("57");

        // invoke the client
        LOG.info("Sending inventory");
        rider.updateInventory(inventory);
        LOG.info("Sending inventory DONE");
    }

    @Test
    public void testStarterShipping() throws Exception {
        // get the proxied interface for the client to use
        RiderService rider = context.getRegistry().lookupByNameAndType("rider", RiderService.class);

        // invoke client to have shipping details returned
        List<ShippingDetail> details = rider.shipInventory("1234", "4444");
        LOG.info("Received shipping details");

        // assert the returned data
        assertEquals(2, details.size());
        assertEquals("Rider Road 66", details.get(0).getAddress());
        assertEquals("Ocean View 123", details.get(1).getAddress());

        // log the returned data to console
        for (ShippingDetail detail : details) {
            LOG.info(detail);
        }
    }

}
