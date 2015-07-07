package camelinaction;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Testing Multicast EIP in regular mode (not concurrent) which should be
 * slower than the MulticastParallelTest.
 */
public class MulticastTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/route.xml");
    }

    @Test
    public void testMulticast() throws Exception {
        String out = template.requestBody("direct:portal", "123", String.class);
        System.out.println(out);
    }
}
