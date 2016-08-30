package camelinaction;

import java.net.UnknownHostException;
import javax.inject.Singleton;

import org.apache.camel.PropertyInject;
import org.apache.camel.util.InetAddressUtil;

/**
 * Define this bean as singleton so CDI can use it for dependency injection
 */
@Singleton
public class HelloBean {

    // inject the Camel property placeholder with the key reply
    @PropertyInject("reply")
    private String reply;

    public String sayHello() throws UnknownHostException {
        // create a reply message which includes the hostname
        return reply + " from " + InetAddressUtil.getLocalHostName();
    }
}
