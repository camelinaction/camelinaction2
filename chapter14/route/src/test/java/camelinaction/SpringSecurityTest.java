package camelinaction;

import javax.security.auth.Subject;

import org.apache.camel.CamelAuthorizationException;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.Exchange;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public class SpringSecurityTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext(new String[]{"META-INF/spring/spring-security.xml"});
    }

    private void sendMessageWithAuth(String uri, String body, String username, String password) {
        Authentication authToken =  new UsernamePasswordAuthenticationToken(username, password);
        
        Subject subject = new Subject();
        subject.getPrincipals().add(authToken);

        template.sendBodyAndHeader(uri, body, Exchange.AUTHENTICATION, subject);    
    }
    
    @Test
    public void testAdminOnly() throws Exception {
        getMockEndpoint("mock:secure").expectedBodiesReceived("Davs Claus!");
        getMockEndpoint("mock:unsecure").expectedBodiesReceived("Davs Claus!", "Hello Jon!");      
        
        sendMessageWithAuth("direct:start", "Davs Claus!", "claus", "secret");
        try {
        sendMessageWithAuth("direct:start", "Hello Jon!", "jon", "secret");        
        } catch (CamelExecutionException e) {
            assertIsInstanceOf(CamelAuthorizationException.class, e.getCause());
        }

        assertMockEndpointsSatisfied();        
    }

}
