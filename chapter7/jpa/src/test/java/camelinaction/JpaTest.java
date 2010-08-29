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

import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jpa.JpaEndpoint;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.spring.SpringCamelContext;
import org.apache.camel.spring.SpringRouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.jpa.JpaTemplate;

public class JpaTest extends CamelTestSupport {

    protected ApplicationContext applicationContext;
    protected JpaTemplate jpaTemplate;

    @Test
    public void testRouteJpa() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMessageCount(1);

        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setName("motor");
        purchaseOrder.setAmount(1);
        purchaseOrder.setCustomer("honda");
        
        template.sendBody("jms:accounting", purchaseOrder);

        assertMockEndpointsSatisfied();
        assertEntityInDB();
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext("META-INF/spring/camel-context.xml");
        return SpringCamelContext.springCamelContext(applicationContext);
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new SpringRouteBuilder() {
            public void configure() {
                from("jms:accounting")
                .to("jpa:camelinaction.PurchaseOrder")
                .to("mock:result");
            }
        };
    }

    @SuppressWarnings("unchecked")
	private void assertEntityInDB() throws Exception {
        JpaEndpoint endpoint = (JpaEndpoint) context.getEndpoint("jpa:camelinaction.PurchaseOrder");        
        jpaTemplate = endpoint.getTemplate();

        List list = jpaTemplate.find("select x from camelinaction.PurchaseOrder x");
        assertEquals(1, list.size());
        
        assertIsInstanceOf(PurchaseOrder.class, list.get(0));
    }
}
