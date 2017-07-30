package camelinaction;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringBigFileSedaTest extends CamelSpringTestSupport {

    @Test
    public void testBigFile() throws Exception {
        // use 60 sec shutdown timeout
        context.getShutdownStrategy().setTimeout(60);

        long start = System.currentTimeMillis();

        // since we use seda the file route is complete before the update
        // as the pending message will stack up a the seda:update queue
        // hence we just let Camel try to shutdown itself and as it does
        // this graceful it will only shutdown when all the messages
        // on the seda queues has been processed
        Thread.sleep(1000);
        context.stop();

        long delta = System.currentTimeMillis() - start;
        System.out.println("Took " + delta / 1000 + " seconds");
    }

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/BigFileSedaTest.xml");
    }
}
