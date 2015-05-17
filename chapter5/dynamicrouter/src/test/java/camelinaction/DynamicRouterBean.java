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
import org.apache.camel.Header;

/**
 * Bean which implements the logic where the message should be routed by the Dynamic Router EIP.
 *
 * @version $Revision$
 */
public class DynamicRouterBean {

    /**
     * The method invoked by Dynamic Router EIP to compute where to go next.
     *
     * @param body          the message body
     * @param previous   the previous endpoint, is <tt>null</tt> on the first invocation
     * @return endpoint uri where to go, or <tt>null</tt> to indicate no more
     */
    public String route(String body, @Header(Exchange.SLIP_ENDPOINT) String previous) {
        return whereToGo(body, previous);
    }

    /**
     * Method which computes where to go next
     */
    private String whereToGo(String body, String previous) {
        if (previous == null) {
            // 1st time
            return "mock://a";
        } else if ("mock://a".equals(previous)) {
            // 2nd time - transform the message body using the simple language
            return "language://simple:Bye ${body}";
        } else {
            // no more, so return null to indicate end of dynamic router
            return null;
        }
    }

}
