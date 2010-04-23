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

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Unit test to show how to use Camel properties component with the Java DSL.
 * We test the Hello World example of integration kits, which is copying a file.
 * <p/>
 * This time we use Camel property placeholders in the route.
 *
 * @version $Revision$
 */
public class CamelRiderJavaDSLProdTest extends CamelTestSupport {

    private String inboxDir;
    private String outboxDir;

    @EndpointInject(uri = "file:{{file.inbox}}")
    private ProducerTemplate inbox;

    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = super.createCamelContext();

        // setup the properties component to use the production file
        PropertiesComponent prop = context.getComponent("properties", PropertiesComponent.class);
        prop.setLocation("classpath:rider-prod.properties");

        return context;
    }

    public void setUp() throws Exception {
        super.setUp();

        // lookup these endpoints from the properties file using Camel property placeholders - {{key}}
        inboxDir = context.resolvePropertyPlaceholders("{{file.inbox}}");
        outboxDir = context.resolvePropertyPlaceholders("{{file.outbox}}");

        // delete directories so we have a clean start
        deleteDirectory(inboxDir);
        deleteDirectory(outboxDir);
    }

    @Test
    public void testCopyFile() throws Exception {
        context.setTracing(true);

        // create a new file in the inbox folder with the name hello.txt and containing Hello World as body
        inbox.sendBodyAndHeader("Hello World", Exchange.FILE_NAME, "hello.txt");

        // wait a while to let the file be moved
        Thread.sleep(1000);

        // test the file was copied
        File target = new File(outboxDir + "/hello.txt");
        assertTrue("File should have been copied", target.exists());

        // test that its content is correct as well
        String content = context.getTypeConverter().convertTo(String.class, target);
        assertEquals("Hello World", content);
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // the route is very simple, notice it uses the placeholders
                from("file:{{file.inbox}}")
                    .to("file:{{file.outbox}}");
            }
        };
    }
}