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

import java.util.concurrent.ExecutorService;

import org.apache.camel.AsyncCallback;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultAsyncProducer;

/**
 * Producer to simulate asynchronous communication with ERP system.

 * @version $Revision$
 */
public class ErpProducer extends DefaultAsyncProducer {

    // use a thread pool for async communication with ERP
    private ExecutorService executor;

    public ErpProducer(Endpoint endpoint) {
        super(endpoint);
        // use Camel to create the thread pool for us
        this.executor = endpoint.getCamelContext().getExecutorServiceStrategy().newFixedThreadPool(this, "ERP", 10);
    }

    public boolean process(final Exchange exchange, final AsyncCallback callback) {
        // simulate async communication using a thread pool in which will return a reply in 5 seconds.
        executor.submit(new Runnable() {
            public void run() {
                log.info("Calling ERP");
                // simulate communication with ERP takes 5 seconds
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    // ignore
                }
                log.info("ERP reply received");

                // set reply
                String in = exchange.getIn().getBody(String.class);
                exchange.getOut().setBody(in + ";516");

                // notify callback we are done
                // must must use done(false) because this method returned false
                log.info("Continue routing");
                callback.done(false);
            }
        });

        // return false to tell Camel that we process asynchronously
        // which enables the Camel routing engine to know this and act accordingly
        log.info("Returning false");
        return false;
    }
}
