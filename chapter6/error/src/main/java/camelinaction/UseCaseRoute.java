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

import java.io.IOException;

import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpOperationFailedException;

/**
 * The use case route.
 *
 * @version $Revision$
 */
public class UseCaseRoute extends RouteBuilder {

    // inject the endpoints to use

    @EndpointInject(ref = "fileEndpoint")
    private Endpoint file;

    @EndpointInject(ref = "httpEndpoint")
    private Endpoint http;

    @EndpointInject(ref = "ftpEndpoint")
    private Endpoint ftp;

    @Override
    public void configure() throws Exception {
        getContext().setTracing(true);

        // general error handler
        errorHandler(defaultErrorHandler()
            .maximumRedeliveries(5)
            .redeliveryDelay(2000)
            .retryAttemptedLogLevel(LoggingLevel.WARN));

        // in case of a http exception then retry at most 3 times
        // and if exhausted then upload using ftp instead
        onException(IOException.class, HttpOperationFailedException.class)
            .maximumRedeliveries(3)
            .handled(true)
            .to(ftp);

        // poll files send them to the HTTP server
        from(file).to(http);
    }

}
