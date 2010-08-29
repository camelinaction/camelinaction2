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

import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The sample example as {@link AggregateABCInvalidTest} but using Spring XML instead.
 * <p/>
 * Please see code comments in the other example.
 *
 * @see camelinaction.MyEndAggregationStrategy
 * @version $Revision$
 */
public class SpringAggregateABCInvalidTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/aggregate-abc-invalid.xml");
    }

    @Test
    public void testABCInvalid() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        // we expect ABC in the published message
        // notice: Only 1 message is expected
        mock.expectedBodiesReceived("ABC");

        // send the first message
        template.sendBodyAndHeader("direct:start", "A", "myId", 1);
        // send the 2nd message with the same correlation key
        template.sendBodyAndHeader("direct:start", "B", "myId", 1);
        // the F message has another correlation key
        template.sendBodyAndHeader("direct:start", "F", "myId", 2);
        // this message has no correlation key so its ignored
        template.sendBody("direct:start", "C");
        // so we resend the message this time with a correlation key
        template.sendBodyAndHeader("direct:start", "C", "myId", 1);

        assertMockEndpointsSatisfied();
    }

}