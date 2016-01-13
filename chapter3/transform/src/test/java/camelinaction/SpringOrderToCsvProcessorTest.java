package camelinaction;

import java.io.File;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringOrderToCsvProcessorTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("camelinaction/SpringOrderToCsvProcessorTest.xml");
    }

    @Test
    public void testOrderToCsvProcessor() throws Exception {
        // this is the inhouse format we want to transform to CSV
        String inhouse = "0000004444000001212320091208  1217@1478@2132";
        template.sendBodyAndHeader("direct:start", inhouse, "Date", "20091208");

        File file = new File("target/orders/received/report-20091208.csv");
        assertTrue("File should exist", file.exists());

        // compare the expected file content
        String body = context.getTypeConverter().convertTo(String.class, file);
        assertEquals("0000004444,20091208,0000012123,1217,1478,2132", body);
    }
}
