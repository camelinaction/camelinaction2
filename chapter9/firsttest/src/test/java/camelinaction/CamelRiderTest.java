package camelinaction;

import java.io.File;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Our third unit test with the Camel Test Kit testing Spring XML routes.
 * We test the Hello World example of integration kits, which is moving a file.
 * <p/>
 * This time we use Camel property placeholders in the route.
 */
public class CamelRiderTest extends CamelSpringTestSupport {

    private String inboxDir;
    private String outboxDir;

    @EndpointInject(uri = "file:{{file.inbox}}")
    private ProducerTemplate inbox;

    public void setUp() throws Exception {
        super.setUp();

        // lookup these endpoints from the properties file using Camel property placeholders - {{key}}
        inboxDir = context.resolvePropertyPlaceholders("{{file.inbox}}");
        outboxDir = context.resolvePropertyPlaceholders("{{file.outbox}}");

        // delete directories so we have a clean start
        deleteDirectory(inboxDir);
        deleteDirectory(outboxDir);
    }

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext(new String[]{"camelinaction/rider-camel-prod.xml", "camelinaction/rider-camel-test.xml"});
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
