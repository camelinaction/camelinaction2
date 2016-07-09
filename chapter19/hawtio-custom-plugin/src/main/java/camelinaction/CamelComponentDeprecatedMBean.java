package camelinaction;

import java.util.List;

/**
 * JMX MBean to find deprecated Camel components.
 * <p/>
 * This MBean is what hawtio uses to talk to the backend when it needs
 * to query the JVM for Camel applications that runs deprecated Camel components.
 */
public interface CamelComponentDeprecatedMBean {

    List<String> findDeprecatedComponents();

}
