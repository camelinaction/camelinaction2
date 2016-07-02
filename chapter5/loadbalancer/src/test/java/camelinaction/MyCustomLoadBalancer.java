package camelinaction;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.processor.loadbalancer.SimpleLoadBalancerSupport;

/**
 * A custom load balancer which will pick the first processor for gold messages,
 * and the 2nd processor for other kind of messages.
 * <p/>
 * Notice we extend the {@link SimpleLoadBalancerSupport} which provides
 * all the proper start and stop logic.
 */
public class MyCustomLoadBalancer extends SimpleLoadBalancerSupport {

    public void process(Exchange exchange) throws Exception {
        Processor target = chooseProcessor(exchange);
        target.process(exchange);
    }

    protected Processor chooseProcessor(Exchange exchange) {
        String type = exchange.getIn().getHeader("type", String.class);
        if ("gold".equals(type)) {
            return getProcessors().get(0);
        } else {
            return getProcessors().get(1);
        }
    }
    
}
