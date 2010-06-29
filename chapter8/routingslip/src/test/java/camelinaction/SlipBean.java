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

import org.apache.camel.RoutingSlip;

/**
 * A bean which turns uses the routing slip EIP
 * <p/>
 * When the slip method is invoked Camel will detect the @RoutingSlip
 * and then continue routing using the Routing Slip EIP pattern using the
 * output from the method invocation as the slip header.
 *
 * @version $Revision$
 */
public class SlipBean {

    @RoutingSlip
    public String slip(String body) {
        // always include A
        String answer = "mock:a";

        // extra step if we are cool
        if (body.contains("Cool")) {
            answer += ",mock:b";
        }

        // and always include C as well
        answer += ",mock:c";
        return answer;
    }
}