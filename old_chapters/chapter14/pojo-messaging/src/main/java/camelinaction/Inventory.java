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

import java.io.Serializable;

/**
 * Domain object.
 * <p/>
 * Must be serializable to be transferred over the network (from client to server)
 *
 * @version $Revision: 111 $
 */
public class Inventory implements Serializable {

    private String supplierId;
    private String partId;
    private String name;
    private String amount;

    public Inventory(String supplierId, String partId) {
        this.supplierId = supplierId;
        this.partId = partId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public String getPartId() {
        return partId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String toString() {
        return supplierId + ", " + partId + ", " + name + ", " + amount;
    }

}
