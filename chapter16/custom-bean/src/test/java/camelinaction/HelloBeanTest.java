package camelinaction;

import javax.management.Attribute;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class HelloBeanTest extends CamelTestSupport {

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        JndiRegistry jndi = super.createRegistry();
        jndi.bind("hello", new HelloBean());
        return jndi;
    }

    @Override
    protected boolean useJmx() {
        return true;
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new HelloRoute();
    }

    protected MBeanServer getMBeanServer() {
        return context.getManagementStrategy().getManagementAgent().getMBeanServer();
    }

    @Test
    public void testHelloBean() throws Exception {
        MBeanServer mbeanServer = getMBeanServer();

        ObjectName on = ObjectName.getInstance("org.apache.camel:context=camel-1,type=processors,name=\"bean1\"");
        assertTrue(mbeanServer.isRegistered(on));

        // should inherit the standard set of JMX attributes
        String camelId = (String) mbeanServer.getAttribute(on, "CamelId");
        assertEquals(context.getName(), camelId);

        // should have the custom Greeting attribute
        String greeting = (String) mbeanServer.getAttribute(on, "Greeting");
        assertEquals("Hello", greeting);

        String reply = (String) mbeanServer.invoke(on, "say", null, null);
        assertEquals("Hello", reply);

        // update the attribute
        mbeanServer.setAttribute(on, new Attribute("Greeting", "Bye"));

        // and test that its changed
        reply = (String) mbeanServer.invoke(on, "say", null, null);
        assertEquals("Bye", reply);
    }
}
