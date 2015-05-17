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

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpOperationFailedException;

/**
 * The route for the use case
 *
 * @version $Revision$
 */
public class UseCaseRoute extends RouteBuilder {

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
        onException(HttpOperationFailedException.class).maximumRedeliveries(3)
            .handled(true)
            // use file instead of ftp as its easier to play with
            .to("file:target/ftp/upload");

        // poll files every 5th second and send them to the HTTP server
        from("file:target/rider?delay=5000&readLock=none")
            .to("http://localhost:8765/rider");
    }
}
