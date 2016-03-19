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
package camelinaction.server;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import camelinaction.RestOrderService;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.jaxrs.swagger.Swagger2Feature;

@ApplicationPath("/")
public class RestOrderApplication extends Application {

    private final RestOrderService orderService;

    public RestOrderApplication(RestOrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public Set<Object> getSingletons() {
        Set<Object> answer = new HashSet<>();

        Swagger2Feature swagger = new Swagger2Feature();
        swagger.setBasePath("/");
        swagger.setHost("localhost:9090");
        swagger.setTitle("Order Service");
        swagger.setDescription("Rider Auto Parts Order Service");
        swagger.setVersion("2.0.0");
        swagger.setContact("rider@autoparts.com");

        answer.add(orderService);
        answer.add(new JacksonJsonProvider());
        answer.add(swagger);
        // to turn on verbose logging
        // answer.add(new LoggingFeature());

        return answer;
    }

}
