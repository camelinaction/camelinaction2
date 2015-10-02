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
import javax.persistence.EntityManager;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jpa.JpaEndpoint;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JpaTest extends CamelSpringTestSupport {

    @Test
    public void testRouteJpa() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMessageCount(1);

        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setName("motor");
        purchaseOrder.setAmount(1);
        purchaseOrder.setCustomer("honda");
        
        template.sendBody("seda:accounting", purchaseOrder);

        assertMockEndpointsSatisfied();
        assertEntityInDB();
    }

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/camel-context.xml");
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                // use seda instead of JMS for testing
                from("seda:accounting")
                    .to("jpa:camelinaction.PurchaseOrder")
                    .to("mock:result");
            }
        };
    }

	private void assertEntityInDB() throws Exception {
        JpaEndpoint endpoint = context.getEndpoint("jpa:camelinaction.PurchaseOrder", JpaEndpoint.class);
        EntityManager em = endpoint.getEntityManagerFactory().createEntityManager();

        List list = em.createQuery("select x from camelinaction.PurchaseOrder x").getResultList();
        assertEquals(1, list.size());
        
        assertIsInstanceOf(PurchaseOrder.class, list.get(0));

        em.close();
    }
}
