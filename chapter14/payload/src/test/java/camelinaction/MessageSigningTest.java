package camelinaction;

import java.io.InputStream;
import java.security.KeyStore;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class MessageSigningTest extends CamelTestSupport {

    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = super.createCamelContext();

        KeyStore keystore = loadKeystore("/cia_keystore.jks", "supersecret");
        context.getRegistry().bind("keystore", keystore);
        KeyStore truststore = loadKeystore("/cia_truststore.jks", "supersecret");
        context.getRegistry().bind("truststore", truststore);

        return context;
    }

    public static KeyStore loadKeystore(String file, String password) throws Exception {
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        InputStream in = MessageSigningTest.class.getResourceAsStream(file);
        keystore.load(in, password.toCharArray());
        return keystore;
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
                    .toF("crypto:sign://keystore?keystore=%s&alias=%s&password=%s", 
                         "#keystore", "jon", "secret")
                    .to("mock:signed")
                    .to("direct:verify");
                
                from("direct:verify")
                    .toF("crypto:verify://keystore?keystore=%s&alias=%s&password=%s",
                        "#truststore", "jon", "secret")
                    .to("mock:verified");
            }
        };
    }
}
