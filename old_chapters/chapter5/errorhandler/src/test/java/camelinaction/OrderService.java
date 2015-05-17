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

import java.io.InputStream;

import org.apache.camel.Exchange;
import org.w3c.dom.Document;

/**
 * @version $Revision: 181 $
 */
public class OrderService {

    public String validate(String body) throws OrderValidationException {
        // an order must contain an amount
        if (!(body.contains("amount"))) {
            throw new OrderValidationException("Invalid order");
        }

        // attach the order id 
        return body + ",id=123";
    }

    public String enrich(String body) throws OrderException {
        if (body.contains("ActiveMQ in Action")) {
            throw new OrderException("ActiveMQ in Action is out of stock");
        }

        // attach the order status
        return body + ",status=OK";
    }

    public String toCsv(String body) throws OrderException {
        if (body.contains("xml")) {
            throw new OrderException("xml files not allowed");
        }

        return body.replaceAll("#", ",");
    }

    public void toSoap(Exchange exchange) {
        String body = exchange.getIn().getBody(String.class);
        if (body.contains("ActiveMQ in Action")) {
            // load the soapFault.xml into a DOM
            InputStream is = exchange.getContext().getClassResolver().loadResourceAsStream("camelinaction/soapFault.xml");
            Document dom = exchange.getContext().getTypeConverter().convertTo(Document.class, is);

            // set a fault to indicate a failure
            exchange.getOut().setFault(true);
            exchange.getOut().setBody(dom);
        } else {
            // load the soapOK.xml into a DOM
            InputStream is = exchange.getContext().getClassResolver().loadResourceAsStream("camelinaction/soapOk.xml");
            Document dom = exchange.getContext().getTypeConverter().convertTo(Document.class, is);

            // set a xml reply
            exchange.getOut().setBody(dom);
        }
    }
}
