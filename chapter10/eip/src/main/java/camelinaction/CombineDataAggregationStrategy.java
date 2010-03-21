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
public class CombineDataAggregationStrategy implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        // to contains the endpoint which we send to so we know which system
        // has returned data back to us
        String to = newExchange.getProperty(Exchange.TO_ENDPOINT, String.class);
        if (to.contains("erp")) {
            return aggregate("ERP", oldExchange, newExchange);
        } else if (to.contains("crm")) {
            return aggregate("CRM", oldExchange, newExchange);
        } else {
            return aggregate("SHIPPING", oldExchange, newExchange);
        }
    }

    public Exchange aggregate(String system, Exchange oldExchange, Exchange newExchange) {
        // the first time oldExchange is null so we got to look out for that
        Exchange answer = oldExchange == null ? newExchange : oldExchange;
        // store data temporary in headers so we can combine data later
        answer.getIn().setHeader(system, newExchange.getIn().getBody());
        return answer;
    }

}
