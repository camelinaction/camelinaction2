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

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * @version $Revision$
 */
public class OrderToCsvBeanTest extends CamelTestSupport {

    @Test
    public void testOrderToCsvBean() throws Exception {
        // this is the inhouse format we want to transform to CSV
        String inhouse = "0000005555000001144120091209  2319@1108";
        template.sendBodyAndHeader("direct:start", inhouse, "Date", "20091209");

        File file = new File("target/orders/received/report-20091209.csv");
        assertTrue("File should exist", file.exists());

        // compare the expected file content
        String body = context.getTypeConverter().convertTo(String.class, file);
        assertEquals("000000555,20091209,000001144,2319,1108", body);
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                    // format inhouse to csv using a bean
                    .bean(new OrderToCsvBean())
                    // and save it to a file
                    .to("file://target/orders/received?fileName=report-${header.Date}.csv");
            }
        };
    }
}
