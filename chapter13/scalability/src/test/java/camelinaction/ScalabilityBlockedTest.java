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

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class ScalabilityBlockedTest extends CamelTestSupport {

    @Override
    protected boolean useJmx() {
        return true;
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = super.createCamelContext();
        context.addComponent("bogus", new BogusComponent());
        return context;
    }

    @Test
    public void testNotBlocked() throws Exception {
        String out = template.requestBody("direct:start", "Camel in Action", String.class);
        assertEquals("Camel in Action;516", out);
    }

    @Test
    public void testBlocked() throws Exception {
        log.info("This operation will cause Camel to block processing the message.");
        log.info("You need to use jconsole to manually unblock this");
        try {
            template.requestBody("direct:start", "ActiveMQ in Action", String.class);
            fail("Should fail");
        } catch (Exception e) {
            // should happen
        }
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                    .log("Calling bogs with ${threadName}")
                    .to("bogus:foo").id("to-bogus")
                    .log("Response from bogus ${body} from ${threadName}");
            }
        };
    }
}
