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

import junit.framework.TestCase;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.junit.Test;

/**
 * Using {@link org.apache.camel.impl.SimpleRegistry} as the Camel {@link org.apache.camel.spi.Registry}
 * to register beans and let Camel lookup them to be used in routes.
 *
 * @version $Revision$
 */
public class SimpleRegistryTest extends TestCase {

    private CamelContext context;
    private ProducerTemplate template;

    @Override
    protected void setUp() throws Exception {
        // create the registry to be the SimpleRegistry which is just a Map based implementation
        SimpleRegistry registry = new SimpleRegistry();
        // register our HelloBean under the name hello
        registry.put("hello", new HelloBean());

        // tell Camel to use our SimpleRegistry
        context = new DefaultCamelContext(registry);

        // create a producer template to use for testing
        template = context.createProducerTemplate();

        // add the route using an inlined RouteBuilder
        context.addRoutes(new RouteBuilder() {
            public void configure() throws Exception {
                from("direct:hello").beanRef("hello");
            }
        });
        // star Camel
        context.start();
    }

    @Override
    protected void tearDown() throws Exception {
        // cleanup resources after test
        template.stop();
        context.stop();
    }

    @Test
    public void testHello() throws Exception {
        // test by sending in World and expect the reply to be Hello World
        Object reply = template.requestBody("direct:hello", "World");
        assertEquals("Hello World", reply);
    }

}
