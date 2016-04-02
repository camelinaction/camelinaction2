package camelinaction;

import java.io.File;
import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.cdi.Uri;
import org.apache.camel.test.cdi.CamelCdiRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.apache.camel.test.junit4.TestSupport.deleteDirectory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Our first unit test with the Camel Test Kit.
 * We test the Hello World example of integration kits, which is moving a file.
 */
@RunWith(CamelCdiRunner.class)
public class FirstTest {

    @Inject
    private CamelContext context;

    @Inject @Uri("file:target/inbox")
    private ProducerTemplate template;

    @Before
    public void setUp() throws Exception {
        // delete directories so we have a clean start
        deleteDirectory("target/inbox");
        deleteDirectory("target/outbox");
    }

    @Test
    public void testMoveFile() throws Exception {
        // create a new file in the inbox folder with the name hello.txt and containing Hello World as body
        template.sendBodyAndHeader("Hello World", Exchange.FILE_NAME, "hello.txt");

        // wait a while to let the file be moved
        Thread.sleep(2000);

        // test the file was moved
        File target = new File("target/outbox/hello.txt");
        assertTrue("File should have been moved", target.exists());

        // test that its content is correct as well
        String content = context.getTypeConverter().convertTo(String.class, target);
        assertEquals("Hello World", content);
    }

}
