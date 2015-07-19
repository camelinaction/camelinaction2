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

import java.security.SignatureException;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.builder.RouteBuilder;
import org.junit.Test;

public class ManInTheMiddleTest extends MessageSigningTest {
        
    @Test
    public void testSignAndVerifyMessage() throws Exception {
        getMockEndpoint("mock:signed").expectedBodiesReceived("Hello World");

        try {
            template.sendBody("direct:sign", "Hello World");            
        } catch (CamelExecutionException e) {
            assertMockEndpointsSatisfied();
            assertIsInstanceOf(SignatureException.class, e.getCause());
        }
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:sign")
                    .to("crypto:sign://keystore?keystore=#keystore&alias=jon&password=secret")
                    .to("mock:signed")
                    .to("direct:mitm");

                from("direct:mitm")
                    .setBody().simple("I'm hacked!")
                    .to("direct:verify");
                
                from("direct:verify")
                    .to("crypto:verify://keystore?keystore=#truststore&alias=jon&password=secret")
                    .to("mock:verified");
            }
        };
    }
}
