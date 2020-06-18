package camelinaction;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpComponent;
import org.apache.camel.support.jsse.KeyManagersParameters;
import org.apache.camel.support.jsse.KeyStoreParameters;
import org.apache.camel.support.jsse.SSLContextParameters;
import org.apache.camel.support.jsse.TrustManagersParameters;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.junit.Test;

public class HttpsTest extends CamelTestSupport {

    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = super.createCamelContext();
        // turn off verifying hostname as this is just a self-signed certificate (usually you should not do this in production)
        ((HttpComponent) context.getComponent("https")).setX509HostnameVerifier(new NoopHostnameVerifier());

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

        context.getRegistry().bind("ssl", sslContextParameters);

        return context;
    }

    // this will utilize the truststore we defined in sslContextParameters bean to access the HTTPS endpoint
    @Test
    public void testHttps() throws Exception {
        String reply = template.requestBody("https://localhost:8080/early?sslContextParameters=#ssl", "Hi Camel!", String.class);
        assertEquals("Hi", reply);
    }

    // we didn't provide any truststore information so the server won't let us connect
    @Test(expected = CamelExecutionException.class)
    public void testHttpsNoTruststore() throws Exception {
        String reply = template.requestBody("https://localhost:8080/early", "Hi Camel!", String.class);
        assertEquals("Hi", reply);
    }
    
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("jetty:https://localhost:8080/early?sslContextParameters=#ssl")
                    .transform().constant("Hi");
            }
        };
    }
}
