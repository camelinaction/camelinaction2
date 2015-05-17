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

import java.net.ConnectException;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * @version $Revision$
 */
public class OnExceptionTest extends CamelTestSupport {

    @Override
    public boolean isUseRouteBuilder() {
        // each unit test include their own route builder
        return false;
    }

    /**
     * This test shows that a direct match will of course trigger the onException
     */
    @Test
    public void testOnExceptionDirectMatch() throws Exception {
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                context.setTracing(true);

                onException(OrderFailedException.class).maximumRedeliveries(3);

                from("direct:order")
                    .bean(OrderServiceBean.class, "handleOrder");
            }
        });
        context.start();

        try {
            template.requestBody("direct:order", "ActiveMQ in Action");
            fail("Should throw an exception");
        } catch (CamelExecutionException e) {
            assertIsInstanceOf(OrderFailedException.class, e.getCause());
        }
    }


    /**
     * This test shows that a wrapped connection exception in OrderFailedException will still
     * be triggered by the onException.
     */
    @Test
    public void testOnExceptionWrappedMatch() throws Exception {
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                context.setTracing(true);

                onException(OrderFailedException.class).maximumRedeliveries(3);

                from("direct:order")
                    .bean(OrderServiceBean.class, "handleOrder")
                    .bean(OrderServiceBean.class, "saveToDB");
            }
        });
        context.start();

        try {
            template.requestBody("direct:order", "Camel in Action");
            fail("Should throw an exception");
        } catch (CamelExecutionException e) {
            assertIsInstanceOf(OrderFailedException.class, e.getCause());
            assertIsInstanceOf(ConnectException.class, e.getCause().getCause());
        }
    }

}
