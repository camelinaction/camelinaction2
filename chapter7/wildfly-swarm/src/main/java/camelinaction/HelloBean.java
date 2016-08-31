package camelinaction;

import javax.inject.Singleton;

import org.apache.camel.PropertyInject;
import org.apache.camel.util.InetAddressUtil;

/**
 * Define this bean as singleton so CDI can use it for dependency injection
 */
@Singleton
public class HelloBean {

    // use @PropertyInject("reply") to inject the property placeholder with the key: reply
    @PropertyInject("reply")
    private String msg;

    public String sayHello() throws Exception {
        // create a reply message which includes the hostname
        return msg + " from " + InetAddressUtil.getLocalHostName();
    }
}
