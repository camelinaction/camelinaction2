package camelinaction;

import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The Spring based example of {@link SplitterBeanTest}.
 * See this class for more details.
 */
public class SpringSplitterBeanTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/splitter-bean.xml");
    }

    @Test
    public void testSplitBean() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:split");
        mock.expectedMessageCount(3);

        Customer customer = CustomerService.createCustomer();

        template.sendBody("direct:start", customer);

        assertMockEndpointsSatisfied();
    }

}
