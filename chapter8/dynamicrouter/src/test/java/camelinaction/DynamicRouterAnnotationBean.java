package camelinaction;

import org.apache.camel.DynamicRouter;
import org.apache.camel.Exchange;
import org.apache.camel.Header;

/**
 * Bean which implements the logic where the message should be routed by the Dynamic Router EIP.
 * <p/>
 * Notice the bean has been annotated with @DynamicRouter
 *
 * @version $Revision$
 */
public class DynamicRouterAnnotationBean {

    /**
     * The method invoked by Dynamic Router EIP to compute where to go next.
     * <p/>
     * Notice this method has been annotated with @DynamicRouter which means Camel turns this method
     * invocation into a Dynamic Router EIP automatically.
     *
     * @param body     the message body
     * @param previous the previous endpoint, is <tt>null</tt> on the first invocation
     * @return endpoint uri where to go, or <tt>null</tt> to indicate no more
     */
    @DynamicRouter
    public String route(String body, @Header(Exchange.SLIP_ENDPOINT) String previous) {
        if (previous == null) {
            // 1st time
            return "mock://a";
        } else if ("mock://a".equals(previous)) {
            // 2nd time - transform the message body using the simple language
            return "language://simple:Bye ${body}";
        } else {
            // no more, so return null to indicate end of dynamic router
            return null;
        }
    }
}