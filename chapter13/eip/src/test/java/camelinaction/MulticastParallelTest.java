package camelinaction;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Testing Multicast EIP in parallel mode (concurrent) which should be
 * fast than the regular MulticastTest.
 */
public class MulticastParallelTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/route-parallel.xml");
    }

    @Test
    public void testMulticastParallel() throws Exception {
        String out = template.requestBody("direct:portal", "123", String.class);
        System.out.println(out);
    }
}
