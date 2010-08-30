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

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.SpringRouteBuilder;
import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @version $Revision$
 */
public class TXToNonTXTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("spring-context.xml");
    }

    @Override
    protected int getExpectedRouteCount() {
        // use 0 as we use a Java based route builder directly in this unit test
        return 0;
    }

    @Test
    public void testWithCamel() throws Exception {
        template.sendBody("activemq:queue:a", "Hi Camel");
        // Need to sleep a while to make the test passed
        Thread.sleep(2000);
        String reply = consumer.receiveBody("activemq:queue:b", 10000, String.class);
        assertEquals("Camel rocks", reply);
    }

    @Test
    public void testWithOther() throws Exception {
        template.sendBody("activemq:queue:a", "Superman");

        String reply = consumer.receiveBody("activemq:queue:b", 10000, String.class);
        assertEquals("Hello Superman", reply);
    }

    @Test
    public void testWithDonkey() throws Exception {
        template.sendBody("activemq:queue:a", "Donkey");

        String reply = consumer.receiveBody("activemq:queue:b", 10000, String.class);
        assertNull("There should be no reply", reply);

        reply = consumer.receiveBody("activemq:queue:ActiveMQ.DLQ", 10000, String.class);
        assertNotNull("It should have been moved to DLQ", reply);
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new SpringRouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("activemq:queue:a")
                    .transacted()
                    .to("direct:quote")
                    .to("activemq:queue:b");

                from("direct:quote")
                    .choice()
                        .when(body().contains("Camel"))
                            .transform(constant("Camel rocks"))
                        .when(body().contains("Donkey"))
                            .throwException(new IllegalArgumentException("Donkeys not allowed"))
                    .otherwise()
                        .transform(body().prepend("Hello "));
            }
        };
    }

}
