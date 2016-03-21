package camelinaction;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.api.management.mbean.ManagedThrottlerMBean;

/**
 * A Java class that uses Camel management API to query the throttler EIP at runtime
 * to get the number of current messages that are hold back in the throttler.
 */
public class RiderThrottlerReporter implements CamelContextAware {

    private CamelContext camelContext;

    public CamelContext getCamelContext() {
        return camelContext;
    }

    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    public long reportThrottler() throws Exception {
        // use the JMX management API to get the mbean for the throttler EIP with the id orderThrottler
        ManagedThrottlerMBean throttler = camelContext.getManagedProcessor("orderThrottler", ManagedThrottlerMBean.class);
        // get the number of current throttled messages
        return throttler.getExchangesInflight();
    }
}
