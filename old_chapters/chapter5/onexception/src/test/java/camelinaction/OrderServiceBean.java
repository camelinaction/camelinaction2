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

import java.io.FileNotFoundException;
import java.net.ConnectException;

/**
 * @version $Revision$
 */
public class OrderServiceBean {

    public String handleOrder(String body) throws OrderFailedException {
        if (body.contains("ActiveMQ")) {
            throw new OrderFailedException("Cannot order ActiveMQ");
        }

        return body + ",id=123";
    }

    public void saveToDB(String order) throws OrderFailedException {
        // simulate no connection to DB and throw it wrapped in order failed exception
        throw new OrderFailedException("Cannot store in DB", new ConnectException("Cannot connect to DB"));
    }
    
    public void enrichFromFile(String order) throws OrderFailedException {
        // simulate no file found and throw it wrapped in order failed exception
        throw new OrderFailedException("Cannot load file", new FileNotFoundException("Cannot find file"));
    }

}
