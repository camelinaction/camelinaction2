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
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class MailTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            
            @Override
            public void configure() throws Exception {
                from("imap://claus@localhost?password=secret").to("mock:result");
            }
        };
    }

    @Test
    public void testSendAndRecieveMail() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedBodiesReceived("Yes, Camel rocks!");

        template.sendBody("smtp://jon@localhost?password=secret&to=claus@localhost", "Yes, Camel rocks!");
        
        mock.assertIsSatisfied();
    }

    @Test
    public void testSendAndRecieveMailWithSubjectHeader() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedBodiesReceived("Yes, Camel rocks!");

        template.sendBodyAndHeader("smtp://jon@localhost?password=secret&to=claus@localhost", "Yes, Camel rocks!", 
           "subject", "Does Camel rock?");

        mock.assertIsSatisfied();
    }


}
