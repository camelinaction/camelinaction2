package camelinaction;

import java.io.File;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class OrderToCsvProcessorTest extends CamelTestSupport {

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

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                    // format inhouse to csv using a processor
                    .process(new OrderToCsvProcessor())
                    // and save it to a file
                    .to("file://target/orders/received?fileName=report-${header.Date}.csv");
            }
        };
    }
}
