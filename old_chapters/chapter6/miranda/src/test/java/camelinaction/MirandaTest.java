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
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.util.ObjectHelper;
import org.junit.Test;

/**
 * Unit test to simulate a real component by mocking a TCP server called miranda.
 * <p/>
 * Instead using mock we can return a canned reply acting as if we was communicating
 * with the real component.
 *
 * @version $Revision$
 */
public class MirandaTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("jetty://http://localhost:9080/service/order")
                    .process(new OrderQueryProcessor())
                    .to("mock:miranda")
                    .process(new OrderResponseProcessor());
            }
        };
    }

    @Test
    public void testMiranda() throws Exception {
        context.setTracing(true);

        MockEndpoint mock = getMockEndpoint("mock:miranda");
        mock.expectedBodiesReceived("ID=123");
        mock.whenAnyExchangeReceived(new Processor() {
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setBody("ID=123,STATUS=IN PROGRESS");
            }
        });

        String out = template.requestBody("http://localhost:9080/service/order?id=123", null, String.class);
        assertEquals("IN PROGRESS", out);

        assertMockEndpointsSatisfied();
    }

    private class OrderQueryProcessor implements Processor {
        public void process(Exchange exchange) throws Exception {
            String id = exchange.getIn().getHeader("id", String.class);
            exchange.getIn().setBody("ID=" + id);
        }
    }

    private class OrderResponseProcessor implements Processor {
        public void process(Exchange exchange) throws Exception {
            String body = exchange.getIn().getBody(String.class);
            String reply = ObjectHelper.after(body, "STATUS=");
            exchange.getIn().setBody(reply);
        }
    }

}
