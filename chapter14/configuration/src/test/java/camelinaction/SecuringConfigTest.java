package camelinaction;

import java.io.File;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jasypt.JasyptPropertiesParser;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class SecuringConfigTest extends CamelTestSupport {

    @EndpointInject(uri = "file:target/inbox")
    private ProducerTemplate inbox;

    private FtpServerBean ftp = new FtpServerBean();
    
    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = super.createCamelContext();

     // create the jasypt properties parser
        JasyptPropertiesParser jasypt = new JasyptPropertiesParser();
        // and set the master password
        jasypt.setPassword("supersecret");   
        
        // we can avoid keeping the master password in plaintext in the application
        // by referencing a environment variable
        // export CAMEL_ENCRYPTION_PASSWORD=supersecret
        // jasypt.setPassword("sysenv:CAMEL_ENCRYPTION_PASSWORD");
        
        // setup the properties component to use the production file
        PropertiesComponent prop = context.getComponent("properties", PropertiesComponent.class);
        prop.setLocation("classpath:rider-test.properties");

        // and use the jasypt properties parser so we can decrypt values
        prop.setPropertiesParser(jasypt);
        
        return context;
    }

    public void setUp() throws Exception {
        super.setUp();
        
        ftp.startServer();
        
        // delete directories so we have a clean start
        deleteDirectory("target/inbox");
        deleteDirectory("target/outbox");
    }

    public void tearDown() throws Exception {
        super.tearDown();
        ftp.shutdownServer();
    };
    
    @Test
    public void testMoveFile() throws Exception {
        context.setTracing(true);

        // create a new file in the inbox folder with the name hello.txt and containing Hello World as body
        inbox.sendBodyAndHeader("Hello World", Exchange.FILE_NAME, "hello.txt");

        // wait a while to let the file be moved
        Thread.sleep(2000);

        // test the file was moved
        File target = new File("target/outbox" + "/hello.txt");
        assertTrue("File should have been moved", target.exists());

        // test that its content is correct as well
        String content = context.getTypeConverter().convertTo(String.class, target);
        assertEquals("Hello World", content);
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // the route is very simple, notice it uses the placeholders
                from("file:target/inbox")
                    .to("ftp://rider:{{ftp.password}}@localhost:21000" 
                        + "/target/outbox");
            }
        };
    }
}
