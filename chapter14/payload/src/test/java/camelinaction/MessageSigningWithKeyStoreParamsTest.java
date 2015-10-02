package camelinaction;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.util.jsse.KeyStoreParameters;
import org.junit.Test;

public class MessageSigningWithKeyStoreParamsTest extends CamelTestSupport {

    @Override
    protected JndiRegistry createRegistry() throws Exception {        
        JndiRegistry registry = super.createRegistry();
        KeyStoreParameters keystore = new KeyStoreParameters();
        keystore.setPassword("supersecret");
        keystore.setResource("./cia_keystore.jks");
        registry.bind("keystore", keystore);
        
        KeyStoreParameters truststore = new KeyStoreParameters();
        truststore.setPassword("supersecret");
        truststore.setResource("./cia_truststore.jks");        
        registry.bind("truststore", truststore);
        return registry;
    }
        
    @Test
    public void testSignAndVerifyMessage() throws Exception {
        getMockEndpoint("mock:signed").expectedBodiesReceived("Hello World");
        getMockEndpoint("mock:verified").expectedBodiesReceived("Hello World");

        template.sendBody("direct:sign", "Hello World");

        assertMockEndpointsSatisfied();
        
        Exchange exchange = getMockEndpoint("mock:signed").getReceivedExchanges().get(0);
        assertNotNull(exchange.getIn().getHeader("CamelDigitalSignature"));
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:sign")
                    .to("crypto:sign://keystore?keyStoreParameters=#keystore&alias=jon&password=secret")
                    .to("mock:signed")
                    .to("direct:verify");
                
                from("direct:verify")
                    .to("crypto:verify://keystore?keyStoreParameters=#truststore&alias=jon&password=secret")
                    .to("mock:verified");
            }
        };
    }
}
