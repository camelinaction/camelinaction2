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

import org.apache.camel.builder.RouteBuilder;

public class FileConsumerRoute extends RouteBuilder {

    private int delay;
    private String name;

    public FileConsumerRoute(String name, int delay) {
        this.name = name;
        this.delay = delay;
    }

    @Override
    public void configure() throws Exception {
        // read files from the shared directory
        from("file:target/inbox" +
                "?delete=true" +
                "&readLock=idempotent" +                   // use idempotent read lock
                "&idempotentRepository=#myRepo" +          // refer to the idempotent repository
                "&readLockLoggingLevel=WARN" +             // logging level, you can set this to DEBUG/OFF for production
                "&shuffle=true")                           // sort the files by random to reduce the chance of multiple nodes trying to process the same file
            .log(name + " - Received file: ${file:name}")
            .delay(delay)
            .log(name + " - Done file:     ${file:name}")
            .to("file:target/outbox");
    }

}
