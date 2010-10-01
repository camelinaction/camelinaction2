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
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

/**
 * A maintenance route which must be started manually to force updating
 * the inventory when a file is dropped into a special folder.
 * <p/>
 * You should start the route using JConsole and stop it again after use.
 * <p/>
 * This example uses onCompletion for stopping the route
 *
 * @version $Revision$
 */
public class ManualRouteWithOnCompletion extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // when the exchange is completed then stop the route by
        // running this onCompletion
        onCompletion().process(new StopRouteProcessor("manual"));

        // ensure we only pickup one file at any given time
        from("file://target/inventory/manual?maxMessagesPerPoll=1")
            // use noAutoStartup to indicate this route should
            // NOT be started when Camel starts
            .routeId("manual").noAutoStartup()
            .log("Doing manual update with file ${file:name}")
            .split(body().tokenize("\n"))
                .convertBodyTo(UpdateInventoryInput.class)
                .to("direct:update")
            .end();
            // use end() to denote the end of the splitter sub-route
    }

    public class StopRouteProcessor implements Processor {
        private final String name;

        public StopRouteProcessor(String name) {
            this.name = name;
        }

        public void process(Exchange exchange) throws Exception {
            // force stopping this route while we are routing an Exchange
            // requires two steps:
            // 1) unregister from the inflight registry
            // 2) stop the route
            exchange.getContext().getInflightRepository().remove(exchange);
            exchange.getContext().stopRoute(name);
        }
    }

}