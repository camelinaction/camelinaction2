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

import java.io.IOException;

import camelinaction.order.InputOrder;
import camelinaction.order.OutputOrder;
import org.apache.camel.Exchange;
import org.apache.camel.Header;

/**
 * @version $Revision$
 */
public class OrderService {

    public String processOrder(InputOrder order, @Header(Exchange.REDELIVERY_COUNTER) int retry) throws Exception {

        // do some processing with the order
        Thread.sleep(1000);

        // simulate error if we refer to a special no
        if (order.getRefNo().equals("FATAL")) {
            throw new IllegalArgumentException("Simulated fatal error");
        }

        if (order.getRefNo().equals("FAIL-ONCE") && retry < 1) {
            throw new IOException("Simulated failing once");
        }

        // return some internal format that the order is ok
        return order.getCustomerId() + ":" + order.getPartId() + ":" + order.getAmount() + "=OK";
    }

    public String toCsv(String line) {
        // just replace : with ,
        return line.replaceAll(":", ",");
    }

    public OutputOrder replyOk() {
        OutputOrder ok = new OutputOrder();
        ok.setCode("OK");
        return ok;
    }

    public OutputOrder replyError(Exception cause) {
        OutputOrder error = new OutputOrder();
        error.setCode("ERROR: " + cause.getMessage());
        return error;
    }


}
