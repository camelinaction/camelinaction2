package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Using a bean in the route to invoke HelloBean.
 */
public class InvokeWithBeanSpringRoute extends RouteBuilder {

    @Autowired
    private HelloBean helloBean;

    @Override
    public void configure() throws Exception {
        from("direct:hello")
            .bean(helloBean, "hello");
    }
}
