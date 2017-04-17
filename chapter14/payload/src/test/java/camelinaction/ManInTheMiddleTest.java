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
                    .toF("crypto:sign://keystore?keystore=%s&alias=%s&password=%s", 
                         "#keystore", "jon", "secret")
                    .to("mock:signed")
                    .to("direct:mitm");
                
                from("direct:mitm")
                    .setBody().simple("I'm hacked!")
                    .to("direct:verify");
                
                from("direct:verify")
                    .toF("crypto:verify://keystore?keystore=%s&alias=%s&password=%s",
                         "#truststore", "jon", "secret")
                    .to("mock:verified");
            }
        };
    }
}
