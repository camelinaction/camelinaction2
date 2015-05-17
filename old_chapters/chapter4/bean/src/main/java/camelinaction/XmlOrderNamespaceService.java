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

import org.apache.camel.Body;
import org.apache.camel.language.Bean;
import org.apache.camel.language.NamespacePrefix;
import org.apache.camel.language.XPath;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Order service using XML namespace in the XPath expression
 *
 * @version $Revision$
 */
public class XmlOrderNamespaceService {

    public Document handleIncomingOrder(@Body Document xml,
                                        @XPath(value = "/c:order/@customerId", 
                                               namespaces = @NamespacePrefix(
                                                   prefix = "c",
                                                   uri = "http://camelinaction.com/order")) int customerId,
                                        @Bean(ref = "guid", method = "generate") int orderId) {

        Attr attr = xml.createAttribute("orderId");
        attr.setValue("" + orderId);

        Node node = xml.getElementsByTagName("order").item(0);
        node.getAttributes().setNamedItem(attr);

        return xml;
    }

}
