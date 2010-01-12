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

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Unit test to demonstrate using a custom {@link org.apache.camel.spi.EventNotifier}.
 *
 * @version $Revision$
 */
public class RiderEventNotifierTest extends CamelTestSupport {

    private DummyRiderFailurePublisher dummy;

    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = super.createCamelContext();

        dummy = new DummyRiderFailurePublisher();

        RiderEventNotifier notifier = new RiderEventNotifier("MyRider");
        notifier.setPublisher(dummy);
        context.getManagementStrategy().setEventNotifier(notifier);

        return context;
    }

    @Test
    public void testOk() throws Exception {
        getMockEndpoint("mock:ok").expectedMessageCount(1);

        template.sendBody("direct:ok", "Camel rocks");

        assertMockEndpointsSatisfied();

        // should be no failures
        assertEquals(0, dummy.getEvents().size());
    }

    @Test
    public void testFailure() throws Exception {
        try {
            template.sendBody("direct:fail", "This should fail");
        } catch (Exception e) {
            // ignore for now
        }

        // should be a single failure published
        assertEquals(1, dummy.getEvents().size());
        DummyRiderFailurePublisher.Event event = dummy.getEvents().get(0);
        assertEquals("MyRider", event.appId);
        assertNotNull(event.eventId);
        assertNotNull(event.timestamp);
        assertEquals("Simulated exception", event.message);
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:ok").to("mock:ok");

                from("direct:fail").throwException(new ConnectException("Simulated exception"));
            }
        };
    }
    
}
