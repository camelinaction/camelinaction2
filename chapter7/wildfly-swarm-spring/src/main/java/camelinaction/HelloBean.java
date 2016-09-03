package camelinaction;

import org.apache.camel.PropertyInject;
import org.apache.camel.util.InetAddressUtil;

/**
 * A plain POJO bean
 */
public class HelloBean {

    // use @PropertyInject("reply") to inject the property placeholder with the key: reply
    @PropertyInject("reply")
    private String msg;

    public String sayHello() throws Exception {
        // create a reply message which includes the hostname
        return msg + " from " + InetAddressUtil.getLocalHostName();
    }
}
