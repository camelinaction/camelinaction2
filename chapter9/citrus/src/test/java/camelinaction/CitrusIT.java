/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package camelinaction;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.junit.JUnit4CitrusTestDesigner;
import com.consol.citrus.jms.message.JmsMessageHeaders;
import org.junit.Test;
import org.springframework.http.HttpStatus;

public class CitrusIT extends JUnit4CitrusTestDesigner {

    @Test
    @CitrusTest
    public void orderStatus() throws Exception {
        description("Checking order 123 should be in progress");

        http().client("statusHttpClient")
                .get("/status?id=123")
                .contentType("text/xml")
                .accept("text/xml")
                .fork(true);

        // JMS receive and response
        receive("statusEndpoint")
                .payload("<order><id>123</id></order>")
                .extractFromHeader(JmsMessageHeaders.CORRELATION_ID, "correlationId");

        send("statusEndpoint")
                .payload("<order><id>123</id><status>In Progress</status></order>")
                .header(JmsMessageHeaders.CORRELATION_ID, "${correlationId}");

        http().client("statusHttpClient")
                .response(HttpStatus.OK)
                .payload("<order><id>123</id><status>In Progress</status></order>")
                .contentType("text/xml")
                .version("HTTP/1.1");
    }

}
