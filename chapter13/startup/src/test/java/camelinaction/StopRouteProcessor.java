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

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Processor to stop a route by its name
 *
 * @version $Revision$
 */
public class StopRouteProcessor implements Processor {

    private final static Log LOG = LogFactory.getLog(StopRouteProcessor.class);

    private final String name;

    /**
     * @param name route to stop
     */
    public StopRouteProcessor(String name) {
        this.name = name;
    }

    public void process(Exchange exchange) throws Exception {
        // force stopping this route while we are routing an Exchange
        // requires two steps:
        // 1) unregister from the inflight registry
        // 2) stop the route
        LOG.info("Stopping route: " + name);
        exchange.getContext().getInflightRepository().remove(exchange);
        exchange.getContext().stopRoute(name);
    }
}
