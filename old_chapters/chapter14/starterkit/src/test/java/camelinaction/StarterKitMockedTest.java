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
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * An example like StarterKitClientTest but not using ActiveMQ but with direct instead.
 * <p/>
 * This is a simple example you can run without any external ActiveMQ broker etc.
 *
 * @version $Revision$
 */
public class StarterKitMockedTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/camel-mocked.xml");
    }

    @Test
    public void testStarterKitUpdateInventory() throws Exception {
        RiderService rider = context.getRegistry().lookupByNameAndType("rider", RiderService.class);
        updateInventory(rider);
    }

    private void updateInventory(RiderService rider) {
        Inventory inventory = new Inventory("1234", "4444");
        inventory.setName("Bumper");
        inventory.setAmount("57");

        log.info("Sending inventory");
        rider.updateInventory(inventory);
    }

    @Test
    public void testStarterShipping() throws Exception {
        RiderService rider = context.getRegistry().lookupByNameAndType("rider", RiderService.class);
        shipInventory(rider);
    }

    private void shipInventory(RiderService rider) {
        List<ShippingDetail> details = rider.shipInventory("1234", "4444");
        log.info("Received shipping details");

        assertEquals(2, details.size());
        assertEquals("Rider Road 66", details.get(0).getAddress());
        assertEquals("Ocean View 123", details.get(1).getAddress());
    }

}
