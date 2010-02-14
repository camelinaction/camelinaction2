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

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;

/**
 * A custom producer.
 *
 * @version $Revision$
 */
public class ERPProducer extends DefaultProducer {

    public ERPProducer(Endpoint endpoint) {
        super(endpoint);
    }

    @Override
    public ERPEndpoint getEndpoint() {
        return (ERPEndpoint) super.getEndpoint();
    }

    public void process(Exchange exchange) throws Exception {
        String input = exchange.getIn().getBody(String.class);

        // if the verbose switch is turned on then log to System out
        if (getEndpoint().isVerbose()) {
            System.out.println("Calling ERP with: " + input);
        }

        // simulate calling ERP system and setting reply on the OUT body
        exchange.getOut().setBody("Simulated response from ERP");
        // support propagating headers (by copying headers from IN -> OUT)
        exchange.getOut().setHeaders(exchange.getIn().getHeaders());
    }

}
