package camelinaction;

import java.io.File;
import java.util.Properties;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Our second unit test with the Camel Test Kit testing Spring XML routes.
 * We test the Hello World example of integration kits, which is moving a file.
 * <p/>
 * This time we use spring property placeholders in the route.
 */
public class SpringRiderTest extends CamelSpringTestSupport {

    private String inboxDir;
    private String outboxDir;

    @EndpointInject(ref = "inbox")
    private ProducerTemplate inbox;

    public void setUp() throws Exception {
        super.setUp();

        Properties prop = context.getRegistry().lookupByNameAndType("properties", Properties.class);
        inboxDir = prop.getProperty("file.inbox");
        outboxDir = prop.getProperty("file.outbox");

        // delete directories so we have a clean start
        deleteDirectory(inboxDir);
        deleteDirectory(outboxDir);
    }

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext(new String[]{"camelinaction/rider-spring-prod.xml", "camelinaction/rider-spring-test.xml"});
    }

    @Test
    public void testMoveFile() throws Exception {
        context.setTracing(true);

        // create a new file in the inbox folder with the name hello.txt and containing Hello World as body
        inbox.sendBodyAndHeader("Hello World", Exchange.FILE_NAME, "hello.txt");

        // wait a while to let the file be moved
        Thread.sleep(2000);

        // test the file was moved
        File target = new File(outboxDir + "/hello.txt");
        assertTrue("File should have been moved", target.exists());

        // test that its content is correct as well
        String content = context.getTypeConverter().convertTo(String.class, target);
        assertEquals("Hello World", content);
    }

}
