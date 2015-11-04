/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package camelinaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.message.Message;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;

import camelinaction.order.Order;
import camelinaction.order.OrderEndpoint;
import camelinaction.order.OrderResult;

public class Client {

    public static void main(String[] args) {
        List<Interceptor<? extends Message>> outInterceptors = new ArrayList<Interceptor<? extends Message>>();

        // Define WSS4j properties for flow outgoing
        Map<String, Object> outProps = new HashMap<String, Object>();
        outProps.put("action", "UsernameToken");
        outProps.put("user", "karaf");
        outProps.put("passwordType", "PasswordText");
        outProps.put("passwordCallbackClass", "camelinaction.StdInPasswordCallback");

        WSS4JOutInterceptor wss4j = new WSS4JOutInterceptor(outProps);
        outInterceptors.add(wss4j);

        // we use CXF to create a client for us as its easier than JAXWS and works
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setOutInterceptors(outInterceptors);
        factory.setServiceClass(OrderEndpoint.class);
        factory.setAddress("http://localhost:9000/order");

        OrderEndpoint client = (OrderEndpoint) factory.create();  
        
        Order order = new Order("motor", 100, "honda");
        System.out.println("Placing order for: " + order);

        OrderResult reply = client.order(order);
        System.out.println("Rider Auto Webservice returned: " + reply.getMessage());
    }
}
