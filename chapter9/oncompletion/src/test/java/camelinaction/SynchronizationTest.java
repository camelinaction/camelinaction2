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
import org.apache.camel.impl.SynchronizationAdapter;
import org.apache.camel.spi.UnitOfWork;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

/**
 * Demonstrates how to use UnitOfWork and Synchronization to do
 * custom log when an Exchange is done.
 * 
 * @version $Revision$
 */
public class SynchronizationTest extends CamelTestSupport {

    private static Log LOG = LogFactory.getLog(SynchronizationTest.class);

    @Test
    public void testGreet() throws Exception {
        String out = template.requestBody("direct:greet", "Camel", String.class);
        assertEquals("Hi Camel. How are you?", out);
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:greet")
                    .process(new Processor() {
                        public void process(Exchange exchange) throws Exception {
                            // add a synchronization hook with our logger
                            exchange.getUnitOfWork().addSynchronization(new MyCompletionLogger());
                        }
                    })
                    .to("log:a")
                    .transform(body().prepend("Hi "))
                    .to("log:b")
                    .transform(body().append(". How are you?"))
                    .to("log:c");
            }
        };
    }

    /**
     * A logger which logs the original input and reply
     */
    private class MyCompletionLogger extends SynchronizationAdapter {

        @Override
        public void onDone(Exchange exchange) {
            // we are lazy and extend SynchronizationAdapter which offers onDone method
            // which is invoked regardless whether Exchange failed or not.

            // use UnitOfWork to get access to the original input message
            UnitOfWork uow = exchange.getUnitOfWork();
            String input = uow.getOriginalInMessage().getBody(String.class);

            // and the reply is in the OUT body
            String reply = exchange.getOut().getBody(String.class);

            // log it
            LOG.info("Exchange is done: in = " + input + ", out = " + reply);
        }
    }
}
