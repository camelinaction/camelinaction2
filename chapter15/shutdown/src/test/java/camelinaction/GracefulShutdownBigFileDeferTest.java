package camelinaction;

import org.apache.camel.Exchange;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class GracefulShutdownBigFileDeferTest extends CamelSpringTestSupport {

    @Override
    public void setUp() throws Exception {
        deleteDirectory("target/inventory/updates");
        super.setUp();
        // use 60 seconds as timeout, as CamelTestSupport uses a 10 sec timeout
        context.getShutdownStrategy().setTimeout(60);
    }

    @Test
    public void testShutdownBigFile() throws Exception {
        String input = createFileBody(30);
        template.sendBodyAndHeader("file:target/inventory/updates", input, Exchange.FILE_NAME, "acme-1.csv");

        // give it some time to pickup and start processing this big file
        Thread.sleep(5000);

        // now we will shutdown
    }

    private String createFileBody(int lines) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines; i++) {
            int no = 58000 + i;
            sb.append("4444," + no + ",Bumper," + i);
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/camel-route-defer-java.xml");
    }

}
