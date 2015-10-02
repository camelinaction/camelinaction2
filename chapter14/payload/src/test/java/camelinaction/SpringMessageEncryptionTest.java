package camelinaction;

import java.security.Key;
import java.security.KeyStore;

import org.apache.camel.Exchange;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.apache.camel.util.jsse.KeyStoreParameters;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringMessageEncryptionTest extends CamelSpringTestSupport {

    // TODO remove this once keystoreparam support added to crypto dataformat
    public static Key loadKey() throws Exception {
        KeyStoreParameters keystore = new KeyStoreParameters();
        keystore.setPassword("supersecret");
        keystore.setResource("./cia_secrets.jceks");
        keystore.setType("JCEKS");
        
        KeyStore store = keystore.createKeyStore();
        return store.getKey("ciasecrets", "secret".toCharArray());
    }
    
    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext(new String[]{"META-INF/spring/message-encryption.xml"});
    }

    @Test
    public void testEncryptAndDecryptMessage() throws Exception {
        getMockEndpoint("mock:unencrypted").expectedBodiesReceived("Hello World");

        template.sendBody("direct:start", "Hello World");

        assertMockEndpointsSatisfied();
        
        Exchange exchange = getMockEndpoint("mock:encrypted").getReceivedExchanges().get(0);
        assertNotEquals("Hello World", exchange.getIn().getBody());
    }
}
