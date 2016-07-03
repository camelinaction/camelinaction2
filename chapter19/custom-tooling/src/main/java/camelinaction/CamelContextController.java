package camelinaction;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.management.AttributeNotFoundException;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.camel.util.CamelVersionHelper;

public final class CamelContextController {

    public static List<ObjectName> findCamelContexts() throws Exception {
        List<ObjectName> answer = new ArrayList<>();

        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        Set<ObjectName> names = server.queryNames(new ObjectName("*:type=context,*"), null);
        for (ObjectName on : names) {

            String id = on.getKeyProperty("name");
            if (id.startsWith("\"") && id.endsWith("\"")) {
                id = id.substring(1, id.length() - 1);
            }

            // filter out older Camel versions as this requires Camel 2.17 or better
            try {
                String version = (String) server.getAttribute(on, "CamelVersion");
                if (CamelVersionHelper.isGE("2.17.0", version)) {
                    answer.add(on);
                }
            } catch (AttributeNotFoundException ex) {
                // ignore
            }
        }
        return answer;
    }

    @SuppressWarnings("unchecked")
    public static List<String> findComponentNames(ObjectName on) throws Exception {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        return (List<String>) server.invoke(on, "findComponentNames", null, null);
    }

}
