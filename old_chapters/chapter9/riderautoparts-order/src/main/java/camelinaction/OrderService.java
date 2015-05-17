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

    public void processOrder(Exchange exchange, InputOrder order,
                             @Header(Exchange.REDELIVERED) Boolean redelivered) throws Exception {

        // simulate CPU processing of the order by sleeping a bit
        Thread.sleep(1000);

        // simulate fatal error if we refer to a special no
        if (order.getRefNo().equals("FATAL")) {
            throw new IllegalArgumentException("Simulated fatal error");
        }

        // simulate fail once if we have not yet redelivered, which means its the first
        // time processOrder method is called
        if (order.getRefNo().equals("FAIL-ONCE") && redelivered == null) {
            throw new IOException("Simulated failing once");
        }

        // processing is okay
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
