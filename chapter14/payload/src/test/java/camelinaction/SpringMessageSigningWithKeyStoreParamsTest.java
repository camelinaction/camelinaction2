package camelinaction;

import org.apache.camel.Exchange;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringMessageSigningWithKeyStoreParamsTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext(new String[]{"META-INF/spring/message-signing.xml"});
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
}
