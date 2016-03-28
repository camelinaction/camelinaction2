/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package camelinaction;

import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.apache.camel.spring.javaconfig.Main;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * The main class that holds the Spring Java Configuration configuration
 * <p/>
 * To automatic add the Camel routes then enable component scan in the package name <tt>camelinaction</tt>
 */
@Configuration
@ComponentScan("camelinaction")
public class MyApplication extends CamelConfiguration {

    /**
     * A main class to run this application as a plain static void main application
     */
    public static void main(String[] args) throws Exception {
        Main main = new Main();
        // confgure the name of the @Configuration class
        main.setConfigClass(MyApplication.class);
        main.run();
    }

}
