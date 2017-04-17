package camelinaction;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringHttpsTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/https.xml");
    }
    
    // this will utilize the truststore we defined in sslContextParameters bean to access the HTTPS endpoint
    @Test
    public void testHttps() throws Exception {
        String reply = template.requestBody("jetty:https://localhost:8080/early?sslContextParameters=#ssl", "Hi Camel!", String.class);
        assertEquals("Hi", reply);
    }

    // we didn't provide any truststore information so the server won't let us connect
    @Test(expected = CamelExecutionException.class)
    public void testHttpsNoTruststore() throws Exception {
        String reply = template.requestBody("jetty:https://localhost:8080/early", "Hi Camel!", String.class);
        assertEquals("Hi", reply);
    }
    
}
