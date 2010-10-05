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

import org.apache.camel.builder.ProxyBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

/**
 * How to use ProxyBuilder to use Camel Proxy in Java DSL.
 * <p/>
 * This example does NOT use Spring at all, which means you can use Camel Proxy without Spring.
 *
 * @version $Revision$
 */
public class StarterKitJavaProxyBuilderTest extends CamelTestSupport {

    private static final Log LOG = LogFactory.getLog(StarterKitJavaProxyBuilderTest.class);

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        // bind the mocked bean with the riderMocked id
        JndiRegistry jndi = super.createRegistry();
        jndi.bind("riderMocked", new RiderAutoPartsMock());
        return jndi;
    }

    @Test
    public void testProxyBuilder() throws Exception {
        // create the proxy with the help of ProxyBuilder
        RiderService rider = new ProxyBuilder(context).endpoint("seda:rider").build(RiderService.class);

        Inventory inventory = new Inventory("1234", "4444");
        inventory.setName("Bumper");
        inventory.setAmount("57");

        LOG.info("Sending inventory");
        rider.updateInventory(inventory);
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("seda:rider")
                    .to("log:rider")
                    .to("bean:riderMocked");
            }
        };
    }

}
