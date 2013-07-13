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

import camelinaction.inventory.UpdateInventoryInput;
import camelinaction.inventory.UpdateInventoryOutput;
import org.apache.camel.Exchange;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @version $Revision$
 */
public class InventoryJavaDSLTest extends CamelSpringTestSupport {

    @Override
    public void setUp() throws Exception {
        deleteDirectory("target/inventory/updates");
        super.setUp();
    }

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/camel-route-java.xml");
    }

    @Test
    public void testSingleWebservice() throws Exception {
        // create input object to send as webservice
        UpdateInventoryInput input = new UpdateInventoryInput();
        input.setSupplierId("4444");
        input.setPartId("57123");
        input.setName("Bumper");
        input.setAmount("50");

        // send the webservice and expect an OK reply
        UpdateInventoryOutput reply = template.requestBody("cxf:bean:inventoryEndpoint", input, UpdateInventoryOutput.class);
        assertEquals("OK", reply.getCode());
    }

    @Test
    public void testSingleFile() throws Exception {
        String input = "4444,57123,Bumper,50\n4444,57124,Fender,87";
        template.sendBodyAndHeader("file:target/inventory/updates", input, Exchange.FILE_NAME, "acme-1.csv");

        Thread.sleep(3000);
    }

}
