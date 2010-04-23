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
import org.apache.camel.component.properties.PropertiesComponent;

/**
 * Unit test to show how to use Camel properties component with the Java DSL.
 * We test the Hello World example of integration kits, which is copying a file.
 * <p/>
 * This unit test is reusing the unit test which was designated to be
 * tested in the production environment. Notice how we extend that class
 * and the only difference is that we set a different location for
 * the properties file on the PropertiesComponent
 *
 * @version $Revision$
 */
public class CamelRiderJavaDSLTest extends CamelRiderJavaDSLProdTest {

    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = super.createCamelContext();

        // setup the properties component to use the test file
        PropertiesComponent prop = context.getComponent("properties", PropertiesComponent.class);
        prop.setLocation("classpath:rider-test.properties");

        return context;
    }

}