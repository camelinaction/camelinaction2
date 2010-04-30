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

import org.apache.camel.CamelExchangeException;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The Spring based example of {@link camelinaction.SplitterStopOnExceptionABCTest}.
 * See this class for more details.
 *
 * @version $Revision$
 */
public class SpringSplitterStopOnExceptionABCTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/splitter-stop.xml");
    }

    @Test
    public void testSplitStopOnException() throws Exception {
        MockEndpoint split = getMockEndpoint("mock:split");
        // we expect 1 messages to be split since the 2nd message should fail
        split.expectedBodiesReceived("Camel rocks");

        // and no combined aggregated message since we stop on exception
        MockEndpoint result = getMockEndpoint("mock:result");
        result.expectedMessageCount(0);

        // now send a message with an unknown letter (F) which forces an exception to occur
        try {
            template.sendBody("direct:start", "A,F,C");
            fail("Should have thrown an exception");
        } catch (CamelExecutionException e) {
            CamelExchangeException cause = assertIsInstanceOf(CamelExchangeException.class, e.getCause());
            IllegalArgumentException iae = assertIsInstanceOf(IllegalArgumentException.class, cause.getCause());
            assertEquals("Key not a known word F", iae.getMessage());
        }

        assertMockEndpointsSatisfied();
    }

}