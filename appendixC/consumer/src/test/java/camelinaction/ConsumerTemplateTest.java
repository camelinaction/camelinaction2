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

import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ConsumerTemplateTest extends CamelSpringTestSupport {

    private static final Log LOG = LogFactory.getLog(ConsumerTemplateTest.class);
    
    protected int getExpectedRouteCount() {
        return 0;
    }

    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/consumerTemplate.xml");
    }

    @Test
    public void testConsumerTemplate() throws Exception {
        // Queue up some messages
        template.sendBody("activemq:orders", "Body1");
        template.sendBody("activemq:orders", "Body2");
        template.sendBody("activemq:orders", "Body3");
        
        OrderCollectorBean collectorBean = getMandatoryBean(OrderCollectorBean.class, "orderCollectorBean");
        assertEquals(",Body1,Body2,Body3", collectorBean.getOrders());
    }

}
