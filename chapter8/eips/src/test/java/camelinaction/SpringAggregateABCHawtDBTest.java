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

import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The ABC example for using persistent Aggregator EIP using Spring XML.
 * <p/>
 * See {@link camelinaction.AggregateABCHawtDBTest} for details.
 *
 * @version $Revision$
 */
public class SpringAggregateABCHawtDBTest extends CamelSpringTestSupport {

    @Test
    public void testABCHawtDB() throws Exception {
        System.out.println("Copy 3 files to target/inbox to trigger the completion");
        System.out.println("Files to copy:\n");
        System.out.println("copy src/test/resources/a.txt target/inbox");
        System.out.println("copy src/test/resources/b.txt target/inbox");
        System.out.println("copy src/test/resources/c.txt target/inbox");
        System.out.println("\nSleeping for 20 seconds");
        System.out.println("\nYou can let the test terminate and then start it again");
        System.out.println("\nwhich should let you be able to continue.");

        Thread.sleep(20 * 1000);
    }

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/aggregate-abc-hawtdb.xml");
    }
}