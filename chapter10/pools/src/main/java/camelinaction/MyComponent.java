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

import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A custom component using ExecutorServiceStrategy to create a thread pool.
 *
 * @version $Revision$
 */
public class MyComponent extends DefaultComponent implements Runnable {

    private static final Log LOG = LogFactory.getLog(MyComponent.class);
    private ScheduledExecutorService executor;

    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        return null;
    }

    public void run() {
        // this is the task being executed every second
        LOG.info("I run now");
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        // create a scheduled thread pool with 1 thread as we only need one task as background task
        executor = getCamelContext().getExecutorServiceManager().newScheduledThreadPool(this, "MyBackgroundTask", 1);
        // schedule the task to run once every second
        executor.scheduleWithFixedDelay(this, 1, 1, TimeUnit.SECONDS);
    }

    @Override
    protected void doStop() throws Exception {
        // shutdown the thread pool
        getCamelContext().getExecutorServiceManager().shutdown(executor);
        super.doStop();
    }

}
