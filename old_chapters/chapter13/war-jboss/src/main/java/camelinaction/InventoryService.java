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

import java.util.Random;

import camelinaction.inventory.UpdateInventoryInput;
import camelinaction.inventory.UpdateInventoryOutput;

/**
 * Various service methods using in this example
 *
 * @version $Revision$
 */
public class InventoryService {

    private Random ran = new Random();

    /**
     * To convert from model to CSV in a simply way
     */
    public String xmlToCsv(UpdateInventoryInput input) {
        return input.getSupplierId() + "," + input.getPartId() + "," + input.getName() + "," + input.getAmount();
    }

    /**
     * To return an OK reply back to the webservice client
     */
    public UpdateInventoryOutput replyOk() {
        UpdateInventoryOutput ok = new UpdateInventoryOutput();
        ok.setCode("OK");
        return ok;
    }

    /**
     * To simulate updating the inventory by calling some external system which takes a bit of time
     */
    public void updateInventory(UpdateInventoryInput input) throws Exception {
        // simulate updating using some CPU processing
        int sleep = ran.nextInt(1000);
        Thread.sleep(sleep);

        System.out.println("Inventory " + input.getPartId() + " updated");
    }

}
