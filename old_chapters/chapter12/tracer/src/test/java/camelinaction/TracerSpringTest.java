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

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Unit test how to use Tracer with Spring XML.
 *
 * @version $Revision$
 */
public class TracerSpringTest extends CamelSpringTestSupport {

    @Override
    public void setUp() throws Exception {
        deleteDirectory("target/rider/orders");
        super.setUp();
    }

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("camelinaction/TracerSpringTest.xml");
    }

    @Test
    public void testTracer() throws Exception {
        template.sendBody("file://target/rider/orders", "123,4444,20100110,222,1");

        String xml = consumer.receiveBody("seda:queue:orders", 5000, String.class);
        assertEquals("<order><id>123/id><customerId>4444/customerId><date>20100110</date>"
                + "<item><id>222</id><amount>1</amount></itemn></order>", xml);
    }

}
