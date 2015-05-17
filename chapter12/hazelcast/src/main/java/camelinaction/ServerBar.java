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

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.apache.camel.main.Main;
import org.apache.camel.processor.idempotent.hazelcast.HazelcastIdempotentRepository;

public class ServerBar {

    private Main main;

    public static void main(String[] args) throws Exception {
        ServerBar bar = new ServerBar();
        bar.boot();
    }

    public void boot() throws Exception {
        // create and embed the hazelcast server
        // (you can use the hazelcast client if you want to connect to external hazelcast server)
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();

        // setup the hazelcast idempontent repository which we will use in the route
        HazelcastIdempotentRepository repo = new HazelcastIdempotentRepository(hz, "camel");

        main = new Main();
        main.enableHangupSupport();
        // bind the hazelcast repository to the name myRepo which we refer to from the route
        main.bind("myRepo", repo);
        // add the route and and let the route be named BAR and use a little delay when processing the files
        main.addRouteBuilder(new FileConsumerRoute("BAR", 100));
        main.run();
    }

}
