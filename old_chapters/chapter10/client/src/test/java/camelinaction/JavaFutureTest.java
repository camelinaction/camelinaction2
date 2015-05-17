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

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import junit.framework.TestCase;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demonstrates how the client concurrency API in Java works
 * for executing tasks in an asynchronous manner.
 *
 * @version $Revision$
 */
public class JavaFutureTest extends TestCase {

    private static Logger LOG = LoggerFactory.getLogger(JavaFutureTest.class);

    @Test
    public void testFutureWithDone() throws Exception {
        // this is the task we want to execute async
        // usually the task is something that takes
        // some time to do
        Callable<String> task = new Callable<String>() {
            public String call() throws Exception {
                // do something that takes some time
                LOG.info("Starting to process task");
                Thread.sleep(5000);
                LOG.info("Task is now done");
                return "Camel rocks";
            }
        };

        // this is the thread pool we will use
        ExecutorService executor = Executors.newCachedThreadPool();

        // now submit the task to the thread pool
        // and get the Future handle back so we can later get the result
        LOG.info("Submitting task to ExecutorService");
        Future<String> future = executor.submit(task);
        LOG.info("Task submitted and we got a Future handle");

        // test when we are done
        boolean done = false;
        while (!done) {
            done = future.isDone();
            LOG.info("Is the task done? " + done);
            if (!done) {
                Thread.sleep(2000);
            }
        }

        // and get the answer
        String answer = future.get();
        LOG.info("The answer is: " + answer);
    }

    @Test
    public void testFutureWithoutDone() throws Exception {
        // this is the task we want to execute async
        // usually the task is something that takes
        // some time to do
        Callable<String> task = new Callable<String>() {
            public String call() throws Exception {
                // do something that takes some time
                LOG.info("Starting to process task");
                Thread.sleep(5000);
                LOG.info("Task is now done");
                return "Camel rocks";
            }
        };

        // this is the thread pool we will use
        ExecutorService executor = Executors.newCachedThreadPool();

        // now submit the task to the thread pool
        // and get the Future handle back so we can later get the result
        LOG.info("Submitting task to ExecutorService");
        Future<String> future = executor.submit(task);
        LOG.info("Task submitted and we got a Future handle");

        // instead of testing when we are done we can just get
        // the result and it will automatic wait until the task is done
        String answer = future.get();
        LOG.info("The answer is: " + answer);
    }

}
