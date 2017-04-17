package camelinaction;

import org.apache.camel.CamelContext;
import org.apache.camel.SSLContextParametersAware;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.util.jsse.KeyManagersParameters;
import org.apache.camel.util.jsse.KeyStoreParameters;
import org.apache.camel.util.jsse.SSLContextParameters;
import org.apache.camel.util.jsse.TrustManagersParameters;
import org.junit.Test;

public class GlobalSSLContextParametersTest extends CamelTestSupport {
    
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = super.createCamelContext();
        context.setSSLContextParameters(createSSLContextParameters());
        ((SSLContextParametersAware) context.getComponent("jetty")).setUseGlobalSslContextParameters(true);
        return context;
    }
    
    private SSLContextParameters createSSLContextParameters() {
        KeyStoreParameters ksp = new KeyStoreParameters();
        ksp.setResource("./cia_keystore.jks");
        ksp.setPassword("supersecret");
        KeyManagersParameters kmp = new KeyManagersParameters();
        kmp.setKeyPassword("secret");
        kmp.setKeyStore(ksp);

        KeyStoreParameters tsp = new KeyStoreParameters();
        tsp.setResource("./cia_truststore.jks");
        tsp.setPassword("supersecret");      
        TrustManagersParameters tmp = new TrustManagersParameters();
        tmp.setKeyStore(tsp);
        
        SSLContextParameters sslContextParameters = new SSLContextParameters();
        sslContextParameters.setKeyManagers(kmp);
        sslContextParameters.setTrustManagers(tmp);
        
        return sslContextParameters;
    }

    // this will utilize the truststore we defined globally using setUseGlobalSslContextParameters to access the HTTPS endpoint
    @Test
    public void testHttps() throws Exception {
        String reply = template.requestBody("jetty:https://localhost:8080/early", "Hi Camel!", String.class);
        assertEquals("Hi", reply);
    }
    
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("jetty:https://localhost:8080/early")
                    .transform().constant("Hi");
            }
        };
    }
}
