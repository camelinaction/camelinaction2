package camelinaction;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

import org.apache.camel.CamelContext;
import org.apache.camel.SSLContextParametersAware;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpComponent;
import org.apache.camel.support.jsse.KeyManagersParameters;
import org.apache.camel.support.jsse.KeyStoreParameters;
import org.apache.camel.support.jsse.SSLContextParameters;
import org.apache.camel.support.jsse.SSLContextServerParameters;
import org.apache.camel.support.jsse.TrustManagersParameters;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.junit.Test;

public class GlobalSSLContextParametersTest extends CamelTestSupport {
    
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = super.createCamelContext();
        context.setSSLContextParameters(createSSLContextParameters());
        ((SSLContextParametersAware) context.getComponent("jetty")).setUseGlobalSslContextParameters(true);
        ((SSLContextParametersAware) context.getComponent("https")).setUseGlobalSslContextParameters(true);
        // turn off verifying hostname as this is just a self-signed certificate (usually you should not do this in production)
        ((HttpComponent) context.getComponent("https")).setX509HostnameVerifier(new NoopHostnameVerifier());
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
        String reply = template.requestBody("https://localhost:8080/early", "Hi Camel!", String.class);
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
