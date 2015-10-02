package camelinaction;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ReverseDataFormatTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("camelinaction/reverse.xml");
    }

    @Test
    public void testMarshal() throws Exception {
        String out = template.requestBody("direct:marshal", "Camel rocks", String.class);
        assertEquals("skcor lemaC", out);
    }

    @Test
    public void testUnmarshal() throws Exception {
        String out = template.requestBody("direct:unmarshal", "skcor lemaC", String.class);
        assertEquals("Camel rocks", out);
    }

}
