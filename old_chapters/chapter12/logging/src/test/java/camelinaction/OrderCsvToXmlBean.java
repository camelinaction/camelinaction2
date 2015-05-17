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

/**
 * A bean for transforming CSV to XML in a quick and dirty way.
 *
 * @version $Revision$
 */
public class OrderCsvToXmlBean {

    public String fromCsvToXml(String body) {
        StringBuilder sb = new StringBuilder();
        sb.append("<order>");

        String[] parts = body.split(",");
        sb.append("<id>").append(parts[0]).append("/id>");
        sb.append("<customerId>").append(parts[1]).append("/customerId>");
        sb.append("<date>").append(parts[2]).append("</date>");
        sb.append("<item>");
        sb.append("<id>").append(parts[3]).append("</id>");
        sb.append("<amount>").append(parts[4]).append("</amount>");
        sb.append("</itemn>");

        sb.append("</order>");
        return sb.toString();
    }

}
