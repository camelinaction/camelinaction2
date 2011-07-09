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

import java.util.concurrent.TimeUnit;

import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @version $Revision$
 */
public class SpringBigFileFixedThreadPoolTest extends CamelSpringTestSupport {

    @Test
    public void testBigFile() throws Exception {
        // when the file route is done (the body is the file)
        NotifyBuilder notify = new NotifyBuilder(context).from("file*")
                .whenAnyDoneMatches(body().isInstanceOf(GenericFile.class)).create();

        long start = System.currentTimeMillis();

        System.out.println("Waiting to be done with 2 min timeout (use ctrl + c to stop)");
        notify.matches(2 * 60, TimeUnit.SECONDS);

        long delta = System.currentTimeMillis() - start;
        System.out.println("Took " + delta / 1000 + " seconds");
    }

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/BigFileFixedThreadPoolTest.xml");
    }
}