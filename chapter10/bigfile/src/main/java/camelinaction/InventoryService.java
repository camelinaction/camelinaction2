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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @version $Revision$
 */
public class InventoryService {

    private static final Log LOG = LogFactory.getLog(InventoryService.class);

    /**
     * Convert the CSV to a model object
     */
    public UpdateInventory csvToObject(String csv) {
        String[] lines = csv.split(",");
        if (lines == null || lines.length != 4) {
            throw new IllegalArgumentException("CSV line is not valid: " + csv);
        }

        String supplierId = lines[0];
        String partId = lines[1];
        String name = lines[2];
        String amount = lines[3];

        return new UpdateInventory(supplierId, partId, name, amount);
    }

    /**
     * To simulate updating the inventory by calling some external system which takes a bit of time
     */
    public void updateInventory(UpdateInventory update) throws Exception {
        // simulate updating using some CPU processing
        Thread.sleep(100);

        LOG.info("Inventory " + update.getPartId() + " updated");
    }

}
