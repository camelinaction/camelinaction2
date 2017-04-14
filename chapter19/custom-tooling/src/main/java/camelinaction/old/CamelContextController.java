package camelinaction.old;

import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Set;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * A controller that can find all running Camel applications in the JVM
 */
public final class CamelContextController {

    /**
     * Find all JMX {@link ObjectName} for all the running Camels in the JVM
     */
    public static Set<ObjectName> findCamelContexts() throws Exception {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        return server.queryNames(new ObjectName("org.apache.camel:type=context,*"), null);
    }

    /**
     * Find all the components in use by the Camel application with the given {@link ObjectName}
     */
    @SuppressWarnings("unchecked")
    public static List<String> findComponentNames(ObjectName on) throws Exception {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        return (List<String>) server.invoke(on, "findComponentNames", null, null);
    }

}
