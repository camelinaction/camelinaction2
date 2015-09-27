package camelinaction.client;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class JmxCamelClient {

    private JMXConnector connector;
    private MBeanServerConnection connection;

    public void connect(String serviceUrl) throws Exception {
        JMXServiceURL url = new JMXServiceURL(serviceUrl);
        connector = JMXConnectorFactory.connect(url, null);
        connection = connector.getMBeanServerConnection();
    }

    public void disconnect() throws Exception {
        connector.close();
    }

    public String getCamelVersion() throws Exception {
        ObjectName on = new ObjectName("org.apache.camel:context=camel-1,type=context,name=\"camel-1\"");
        String version = (String) connection.getAttribute(on, "CamelVersion");
        return version;
    }

    public String getCamelUptime() throws Exception {
        ObjectName on = new ObjectName("org.apache.camel:context=camel-1,type=context,name=\"camel-1\"");
        String version = (String) connection.getAttribute(on, "Uptime");
        return version;
    }
}
