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

import java.net.URL;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.util.jsse.KeyManagersParameters;
import org.apache.camel.util.jsse.KeyStoreParameters;
import org.apache.camel.util.jsse.SSLContextParameters;
import org.junit.Before;
import org.junit.Test;

public class HttpsTest extends CamelTestSupport {

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        KeyStoreParameters ksp = new KeyStoreParameters();
        ksp.setResource(this.getClass().getClassLoader().getResource("./cia_keystore.jks").toString());
        ksp.setPassword("supersecret");

        KeyManagersParameters kmp = new KeyManagersParameters();
        kmp.setKeyPassword("secret");
        kmp.setKeyStore(ksp);

        SSLContextParameters sslContextParameters = new SSLContextParameters();
        sslContextParameters.setKeyManagers(kmp);

        JndiRegistry registry = super.createRegistry();
        registry.bind("sslContextParameters", sslContextParameters);

        return registry;
    }
    
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        URL trustStoreUrl = this.getClass().getClassLoader().getResource("./cia_truststore.jks");
        System.setProperty("javax.net.ssl.trustStore", trustStoreUrl.toURI().getPath());
    }
        
    @Test
    public void testHttps() throws Exception {
        final String body = "Hello Camel";

        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedBodiesReceived(body);

        // send an InOut (= requestBody) to Camel
        log.info("Caller calling Camel with message: " + body);
        String reply = template.requestBody("https://localhost:8080/early", body, String.class);

        // we should get the reply early which means you should see this log line
        // before Camel has finished processed the message
        log.info("Caller finished calling Camel and received reply: " + reply);
        assertEquals("OK", reply);

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("jetty:https://localhost:8080/early?sslContextParametersRef=sslContextParameters").routeId("input")
                    // use wiretap to continue processing the message
                    // in another thread and let this consumer continue
                    .wireTap("direct:incoming")
                    // and return an early reply to the waiting caller
                    .transform().constant("OK");

                from("direct:incoming").routeId("process")
                    // convert the jetty stream to String so we can safely read it multiple times
                    .convertBodyTo(String.class)
                    .log("Incoming ${body}")
                    // simulate processing by delaying 3 seconds
                    .delay(3000)
                    .log("Processing done for ${body}")
                    .to("mock:result");
            }
        };
    }
}
