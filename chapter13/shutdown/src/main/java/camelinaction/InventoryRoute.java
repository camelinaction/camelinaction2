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

import camelinaction.inventory.UpdateInventoryInput;

/**
 * Java DSL routes for the inventory examples showing how to use startupOrder.
 *
 * @version $Revision$
 */
public class InventoryRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // this is the webservice route which is started last
        from("cxf:bean:inventoryEndpoint")
            .routeId("webservice").startupOrder(3)
            .to("direct:update")
            .transform().method("inventoryService", "replyOk");

        // this is the file route which is started 2nd last
        from("file://target/inventory/updates")
            .routeId("file").startupOrder(2)
            .split(body().tokenize("\n"))
            .convertBodyTo(UpdateInventoryInput.class)
            .to("direct:update");

        // this is the shared route which then must be started first
        from("direct:update")
            .routeId("update").startupOrder(1)
            .to("bean:inventoryService?method=updateInventory");
    }
}
