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
import org.apache.camel.builder.RouteBuilder;

/**
 * Java DSL routes for the inventory examples showing how to use startupOrder.
 *
 * @version $Revision$
 */
public class FileInventoryRoute extends RouteBuilder {

    private String inputPath;
    private String routeId;
    
    @Override
    public void configure() throws Exception {
        from(inputPath)
            .routeId(getRouteId())
            .split(body().tokenize("\n"))
            .convertBodyTo(UpdateInventoryInput.class)
            .to("direct:update");
    }

    public String getInputPath() {
        return inputPath;
    }

    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }
}
