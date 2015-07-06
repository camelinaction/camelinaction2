package camelinaction;

import java.util.concurrent.ExecutorService;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringThreadPoolTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/ThreadPoolTest.xml");
    }

    @Test
    public void testThreadPool() throws Exception {
        ExecutorService myPool = context.getRegistry().lookupByNameAndType("myPool", ExecutorService.class);
        assertNotNull(myPool);

        getMockEndpoint("mock:result").expectedMessageCount(1);

        template.sendBody("direct:start", "Hello Camel");

        assertMockEndpointsSatisfied();
    }

}
