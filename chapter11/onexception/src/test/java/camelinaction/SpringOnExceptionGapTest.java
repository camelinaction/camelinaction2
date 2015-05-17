package camelinaction;

import java.io.FileNotFoundException;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * This tests shows no match for onException and therefore fallback on the error handler
 * and by which there are no explicit configured. Therefore default error handler will be
 * used which by default does NO redelivery attempts.
 */
public class SpringOnExceptionGapTest extends CamelSpringTestSupport {

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("camelinaction/SpringOnExceptionGapTest.xml");
    }

    @Test
    public void testOnExceptionGab() throws Exception {
        try {
            template.requestBody("direct:order", "Camel in Action");
            fail("Should throw an exception");
        } catch (CamelExecutionException e) {
            assertIsInstanceOf(OrderFailedException.class, e.getCause());
            assertIsInstanceOf(FileNotFoundException.class, e.getCause().getCause());
        }
    }
}
