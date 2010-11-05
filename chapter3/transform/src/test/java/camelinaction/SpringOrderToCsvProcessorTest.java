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

import java.io.File;

import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @version $Revision: 27 $
 */
public class SpringOrderToCsvProcessorTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("camelinaction/SpringOrderToCsvProcessorTest.xml");
    }

    @Test
    public void testOrderToCsvProcessor() throws Exception {
        // this is the inhouse format we want to transform to CSV
        String inhouse = "0000004444000001212320091208  1217@1478@2132";
        template.sendBodyAndHeader("direct:start", inhouse, "Date", "20091208");

        File file = new File("target/orders/received/report-20091208.csv");
        assertTrue("File should exist", file.exists());

        // compare the expected file content
        String body = context.getTypeConverter().convertTo(String.class, file);
        assertEquals("000000444,20091208,000001212,1217,1478,2132", body);
    }
}
