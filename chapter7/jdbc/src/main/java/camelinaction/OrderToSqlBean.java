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

import org.apache.camel.language.XPath;

public class OrderToSqlBean {

    public String toSql(@XPath("order/@name") String name,
                        @XPath("order/@amount") int amount,
                        @XPath("order/@customer") String customer) {

        StringBuilder sb = new StringBuilder();
        sb.append("insert into incoming_orders (part_name, quantity, customer) values (");
        sb.append("'").append(name).append("', ");
        sb.append("'").append(amount).append("', ");
        sb.append("'").append(customer).append("') ");
        System.out.println(sb.toString());
        return sb.toString();
    }
}
