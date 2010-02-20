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

import camelinaction.inventory.UpdateInventoryInput;
import org.apache.camel.Converter;

/**
 * @version $Revision$
 */
@Converter
public final class InventoryConverter {

    private InventoryConverter() {
    }

    @Converter
    public static UpdateInventoryInput toInput(String csv) {
        String[] lines = csv.split(",");
        if (lines == null || lines.length != 4) {
            throw new IllegalArgumentException("CSV line is not valid: " + csv);
        }

        UpdateInventoryInput input = new UpdateInventoryInput();
        input.setSupplierId(lines[0]);
        input.setPartId(lines[1]);
        input.setName(lines[2]);
        input.setAmount(lines[3]);

        return input;
    }

}
