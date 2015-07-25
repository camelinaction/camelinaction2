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

import java.security.Key;
import java.security.KeyStore;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.crypto.CryptoDataFormat;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.util.jsse.KeyStoreParameters;
import org.junit.Test;

public class MessageEncryptionTest extends CamelTestSupport {

    private Key secretKey;

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        JndiRegistry registry = super.createRegistry();
        
        KeyStoreParameters keystore = new KeyStoreParameters();
        keystore.setPassword("supersecret");
        keystore.setResource("./cia_secrets.jceks");
        keystore.setType("JCEKS");
        
        KeyStore store = keystore.createKeyStore();
        secretKey = store.getKey("ciasecrets", "secret".toCharArray());
        registry.bind("ciasecrets", secretKey);
        return registry;
    }

        
    @Test
    public void testEncryptAndDecryptMessage() throws Exception {
        getMockEndpoint("mock:unencrypted").expectedBodiesReceived("Hello World");

        template.sendBody("direct:start", "Hello World");

        assertMockEndpointsSatisfied();
        
        Exchange exchange = getMockEndpoint("mock:encrypted").getReceivedExchanges().get(0);
        assertNotEquals("Hello World", exchange.getIn().getBody());
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                CryptoDataFormat crypto = new CryptoDataFormat("DES", secretKey);
                
                from("direct:start")
                .marshal(crypto)
                .to("mock:encrypted")
                .unmarshal(crypto)
                .to("mock:unencrypted");
            }
        };
    }
}
