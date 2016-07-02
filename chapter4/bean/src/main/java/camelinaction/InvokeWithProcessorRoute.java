package camelinaction;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

/**
 * Using a Processor in the route to invoke HelloBean.
 */
public class InvokeWithProcessorRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:hello")
            .process(new Processor() {
                public void process(Exchange exchange) throws Exception {
                    // extract the name parameter from the Camel message which we want to use
                    // when invoking the bean
                    String name = exchange.getIn().getBody(String.class);

                    // now create an instance of the bean
                    HelloBean hello = new HelloBean();
                    // and invoke it with the name parameter
                    String answer = hello.hello(name);

                    // store the reply from the bean on the OUT message
                    exchange.getOut().setBody(answer);
                }
            });
    }
}
