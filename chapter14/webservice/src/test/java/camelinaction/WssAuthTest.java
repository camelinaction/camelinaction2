package camelinaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.message.Message;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import camelinaction.order.Order;
import camelinaction.order.OrderEndpoint;
import camelinaction.order.OrderResult;

public class WssAuthTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("wss-auth-route.xml");
    }

    protected static OrderEndpoint createCXFClient(String url, String user, String passwordCallbackClass) {
        List<Interceptor<? extends Message>> outInterceptors = new ArrayList();

        // Define WSS4j properties for flow outgoing
        Map<String, Object> outProps = new HashMap<String, Object>();
        outProps.put("action", "UsernameToken Timestamp");
        outProps.put("user", user);
        outProps.put("passwordCallbackClass", passwordCallbackClass);

        WSS4JOutInterceptor wss4j = new WSS4JOutInterceptor(outProps);
        // Add LoggingOutInterceptor
        LoggingOutInterceptor loggingOutInterceptor = new LoggingOutInterceptor();

        outInterceptors.add(wss4j);
        outInterceptors.add(loggingOutInterceptor);

        // we use CXF to create a client for us as its easier than JAXWS and works
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setOutInterceptors(outInterceptors);
        factory.setServiceClass(OrderEndpoint.class);
        factory.setAddress(url);
        return (OrderEndpoint) factory.create();
    }
    
    @Test
    public void testOrderOk() throws Exception {      
        OrderEndpoint client = createCXFClient("http://localhost:9000/order", "jon", "camelinaction.wssecurity.ClientPasswordCallback");
        OrderResult reply = client.order(new Order("motor", 100, "honda"));
        assertEquals("OK", reply.getMessage());
    }
    
    @Test
    public void testBadUser() throws Exception {
        OrderEndpoint client = createCXFClient("http://localhost:9000/order", "rider", "camelinaction.wssecurity.ClientPasswordCallback");
        try {
            OrderResult reply = client.order(new Order("motor", 100, "honda"));
            Assert.fail();
        } catch (javax.xml.ws.soap.SOAPFaultException e) {
        }
    }

    @Test
    public void testBadPassword() throws Exception {
        OrderEndpoint client = createCXFClient("http://localhost:9000/order", "jon", "camelinaction.wssecurity.BadClientPasswordCallback");
        try {
            OrderResult reply = client.order(new Order("motor", 100, "honda"));
            Assert.fail();
        } catch (javax.xml.ws.soap.SOAPFaultException e) {
        }
    }
}
