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

import org.apache.camel.spring.Main;

/**
 * A Main class to run our Camel application standalone
 *
 * @version $Revision$
 */
public class InventoryMain {

    public static void main(String[] args) throws Exception {
        // use the Main class from camel-spring
        Main main = new Main();
        // to load Spring XML file
        main.setApplicationContextUri("META-INF/spring/camel-context.xml");
        // and enable hangup support which means we can stop nicely when ctrl+c is pressed
        main.enableHangupSupport();
        // and start (will wait until you stop with ctrl + c)
        main.start();
        // echo to console how you can stop
        System.out.println("\n\nApplication has now been started. You can press ctrl + c to stop.\n\n");
    }

}
