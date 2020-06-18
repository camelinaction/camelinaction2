package camelinaction;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.support.jsse.KeyStoreParameters;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class MessageSigningWithKeyStoreParamsTest extends CamelTestSupport {

    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = super.createCamelContext();

        KeyStoreParameters keystore = new KeyStoreParameters();
        keystore.setPassword("supersecret");
        keystore.setResource("./cia_keystore.jks");
        context.getRegistry().bind("keystore", keystore);

        KeyStoreParameters truststore = new KeyStoreParameters();
        truststore.setPassword("supersecret");
        truststore.setResource("./cia_truststore.jks");
        context.getRegistry().bind("truststore", truststore);

        return context;
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
