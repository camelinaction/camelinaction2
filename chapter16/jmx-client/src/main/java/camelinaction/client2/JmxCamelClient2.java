package camelinaction.client2;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.camel.api.management.mbean.ManagedCamelContextMBean;

public class JmxCamelClient2 {

    private JMXConnector connector;
    private MBeanServerConnection connection;
    private ManagedCamelContextMBean proxy;

    public void connect(String serviceUrl) throws Exception {
        JMXServiceURL url = new JMXServiceURL(serviceUrl);
        connector = JMXConnectorFactory.connect(url, null);
        connection = connector.getMBeanServerConnection();

        // create a mbean proxy so we can use the type safe api
        ObjectName on = new ObjectName("org.apache.camel:context=camel-1,type=context,name=\"camel-1\"");
        proxy = JMX.newMBeanProxy(connection, on, ManagedCamelContextMBean.class);
    }

    public void disconnect() throws Exception {
        connector.close();
    }

    public String getCamelVersion() throws Exception {
        return proxy.getCamelVersion();
    }

    public String getCamelUptime() throws Exception {
        return proxy.getUptime();
    }
}
