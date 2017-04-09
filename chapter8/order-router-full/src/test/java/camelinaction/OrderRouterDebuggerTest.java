package camelinaction;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

 public class OrderRouterDebuggerTest extends CamelSpringTestSupport {

    private static final String ORDER = "<order name=\"motor\" amount=\"1\" customer=\"honda\"/>";
     
    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/camel-context.xml");
    }
        
    @Override
    public boolean isUseDebugger() {
        return true;
    }
    
    @Override
    protected void debugBefore(Exchange exchange, Processor processor,
            ProcessorDefinition<?> definition, String id, String shortName) {
        log.info("MyDebugger: before " + definition + " with body " + exchange.getIn().getBody());
    }

    
    @Override
    protected void debugAfter(Exchange exchange, Processor processor, 
            ProcessorDefinition<?> definition, String id, String label, long timeTaken) {
        log.info("MyDebugger: after " + definition + " took " + timeTaken + " ms, with body " + exchange.getIn().getBody());    
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
