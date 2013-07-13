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

import org.apache.camel.Exchange;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @version $Revision$
 */
public class GracefulShutdownBigFileTest extends CamelSpringTestSupport {

    @Override
    public void setUp() throws Exception {
        deleteDirectory("target/inventory/updates");
        super.setUp();
        // use 60 seconds as timeout, as CamelTestSupport uses a 10 sec timeout
        context.getShutdownStrategy().setTimeout(60);
    }

    @Test
    public void testShutdownBigFile() throws Exception {
        String input = createFileBody(30);
        template.sendBodyAndHeader("file:target/inventory/updates", input, Exchange.FILE_NAME, "acme-1.csv");

        // give it some time to pickup and start processing this big file
        Thread.sleep(3000);

        // now we will shutdown
    }

    private String createFileBody(int lines) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines; i++) {
            int no = 58000 + i;
            sb.append("4444," + no + ",Bumper," + i);
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/camel-route-defer-java.xml");
    }

}
