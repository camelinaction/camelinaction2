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

import java.util.concurrent.TimeUnit;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * Main app to demonstrate how to manage Tracer at runtime using JMX
 *
 * @version $Revision$
 */
public class ManageTracerMain {

    private CamelContext context;
    private ProducerTemplate template;

    public static void main(String[] args) throws Exception {
        ManageTracerMain main = new ManageTracerMain();
        main.run();
    }

    public void run() throws Exception {
        context = createCamelContext();
        context.addRoutes(createRouteBuilder());
        template = context.createProducerTemplate();
        context.start();
        testManageTracer();
        template.stop();
        context.stop();
    }

    protected CamelContext createCamelContext() throws Exception {
        CamelContext answer = new DefaultCamelContext();
        // simulate JMS with the Mock component
        answer.addComponent("jms", answer.getComponent("mock"));

        // enable connector for remote management
        answer.getManagementStrategy().getManagementAgent().setCreateConnector(true);

        return answer;
    }

    public void testManageTracer() throws Exception {
        System.out.println("Connect to JConsole and try managing Tracer by enabling and disabling it on individual routes");

        MockEndpoint mock = context.getEndpoint("jms:queue:orders", MockEndpoint.class);
        mock.expectedMessageCount(100);

        for (int i = 0; i < 100; i++) {
            template.sendBody("file://target/rider/orders", "" + i + ",4444,20100110,222,1");
        }

        mock.await(100 * 10, TimeUnit.SECONDS);

        System.out.println("Complete sending 100 files will stop now");
    }

    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // slow things down a bit
                context.setDelayer(2000L);

                from("file://target/rider/orders")
                    .wireTap("seda:audit")
                    .bean(OrderCsvToXmlBean.class)
                    .to("jms:queue:orders");

                from("seda:audit")
                    .bean(AuditService.class, "auditFile");
            }
        };
    }

}
