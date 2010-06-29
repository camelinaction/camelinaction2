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
import org.apache.camel.processor.aggregate.AggregationStrategy;

/**
 * @version $Revision$
 */
public class MyIgnoreFailureAggregationStrategy implements AggregationStrategy {

    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        // check if there was an exception thrown
        if (newExchange.getException() != null) {
            // yes there was, so we just handle it by ignoring it
            return oldExchange;
        }

        if (oldExchange == null) {
            // this is the first time so no existing aggregated exchange
            return newExchange;
        }

        // append the new word to the existing
        String body = newExchange.getIn().getBody(String.class);
        String existing = oldExchange.getIn().getBody(String.class);

        oldExchange.getIn().setBody(existing + "+" + body);
        return oldExchange;
    }
}