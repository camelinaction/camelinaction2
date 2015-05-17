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

import org.apache.camel.ThreadPoolRejectedPolicy;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spi.ThreadPoolProfile;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * How to configure a custom thread pool profile in Java DSL
 *
 * @version $Revision$
 */
public class CustomThreadPoolProfileTest extends CamelTestSupport {

    public ThreadPoolProfile createCustomProfile() {
        // create a custom thread pool profile with the name bigPool
        ThreadPoolProfile profile = new ThreadPoolProfile("bigPool");
        profile.setMaxPoolSize(200);
        profile.setRejectedPolicy(ThreadPoolRejectedPolicy.DiscardOldest);
        return profile;
    }

    @Test
    public void testCustomThreadPoolProfile() throws Exception {
        getMockEndpoint("mock:result").expectedMessageCount(1);

        template.sendBody("direct:start", "Hello Camel");

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // register a custom thread pool profile
                ThreadPoolProfile custom = createCustomProfile();
                context.getExecutorServiceManager().registerThreadPoolProfile(custom);

                from("direct:start")
                    // use the bigPool profile for creating the thread pool to be used
                    .threads().executorServiceRef("bigPool")
                    .to("log:foo")
                    .to("mock:result");
            }
        };
    }
}
