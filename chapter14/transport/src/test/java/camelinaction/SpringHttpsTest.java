package camelinaction;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.component.http.HttpComponent;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringHttpsTest extends CamelSpringTestSupport {

    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = super.createCamelContext();
        // turn off verifying hostname as this is just a self-signed certificate (usually you should not do this in production)
        ((HttpComponent) context.getComponent("https")).setX509HostnameVerifier(new NoopHostnameVerifier());
        return context;
    }

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/https.xml");
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
    
}
