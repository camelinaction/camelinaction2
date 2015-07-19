package camelinaction;

import java.util.concurrent.TimeUnit;

import org.apache.camel.Exchange;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * How to concurrent process files using threads EIP using XML DSL
 */
public class SpringFileThreadsTest extends CamelSpringTestSupport {

    private int files = 100;

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/SpringFileThreadsTest.xml");
    }

    @Test
    public void testFileThreads() throws Exception {
        log.info("Creating {} files...", files);

        // create many files
        for (int i = 0; i < files; i++) {
            template.sendBodyAndHeader("file:target/inbox", "Message " + i, Exchange.FILE_NAME, "file-" + i + ".txt");
        }

        log.info("Starting route");

        // keep track of completed files
        NotifyBuilder notify = new NotifyBuilder(context).whenCompleted(files).create();

        // start route
        context.startRoute("myRoute");

        // wait for all files to be processed
        assertTrue("Should complete all files", notify.matches(60, TimeUnit.SECONDS));
    }

}
