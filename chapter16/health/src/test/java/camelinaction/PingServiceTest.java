package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class PingServiceTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new PingService();
    }

    @Test
    public void testPing() throws Exception {
        String reply = template.requestBody("http://127.0.0.1:8080/ping", null, String.class);
        assertEquals("PONG", reply);
    }

}
