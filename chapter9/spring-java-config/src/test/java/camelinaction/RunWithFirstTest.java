package camelinaction;

import java.io.File;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.test.spring.CamelSpringDelegatingTestContextLoader;
import org.apache.camel.test.spring.CamelSpringRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.apache.camel.test.junit4.TestSupport.deleteDirectory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(CamelSpringRunner.class)
@ContextConfiguration(classes = MyApplication.class, loader = CamelSpringDelegatingTestContextLoader.class)
public class RunWithFirstTest {

    @Autowired
    private CamelContext context;

    @Autowired
    private ProducerTemplate template;

    @Before
    public void cleanDir() throws Exception {
        // delete directories so we have a clean start
        deleteDirectory("target/inbox");
        deleteDirectory("target/outbox");
    }

    @Test
    public void testMoveFile() throws Exception {
        // use NotifyBuilder to wait for the file to be routed
        NotifyBuilder notify = new NotifyBuilder(context).whenDone(1).create();

        // create a new file in the inbox folder with the name hello.txt and containing Hello World as body
        template.sendBodyAndHeader("file://target/inbox", "Hello World", Exchange.FILE_NAME, "hello.txt");

        // notifier will wait for the file to be processed
        // and if that never happen it will time out after 10 seconds (default mock timeout)
        assertTrue(notify.matchesMockWaitTime());

        // test the file was moved
        File target = new File("target/outbox/hello.txt");
        assertTrue("File should have been moved", target.exists());

        // test that its content is correct as well
        String content = context.getTypeConverter().convertTo(String.class, target);
        assertEquals("Hello World", content);
    }

}
