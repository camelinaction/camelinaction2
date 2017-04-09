package camelinaction;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

 public class OrderRouterTest extends CamelSpringTestSupport {

    private static final String ORDER = "<order name=\"motor\" amount=\"1\" customer=\"honda\"/>";
     
    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/camel-context.xml");
    }
        
    @Test
    public void testSendToFtpAndWebService() throws Exception {
        template.sendBody("ftp://rider@localhost:21000/order?password=secret", ORDER);
        
        List<Object> params = new ArrayList<Object>();
        params.add("motor");
        params.add(1);
        params.add("honda");
        
        String reply = template.requestBody("cxf:bean:orderEndpoint", params, String.class);
        assertEquals("OK", reply);
        
        Thread.sleep(2000);
        // should see output on console now
    }

}
