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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A mocked implementation of {@link RiderService} to be used for testing purpose.
 *
 * @version $Revision: 111 $
 */
public class RiderAutoPartsMock implements RiderService {

    private static final Log LOG = LogFactory.getLog(RiderAutoPartsMock.class);

    public void updateInventory(Inventory inventory) {
        LOG.info("Updating inventory " + inventory);
    }

    public List<ShippingDetail> shipInventory(String supplierId, String partId) {
        LOG.info("Shipping to Rider Road 66 and Ocean View 123");

        ShippingDetail detail = new ShippingDetail();
        detail.setAddress("Rider Road 66");
        detail.setCountry("USA");
        detail.setZip("90210");
        detail.setAmount("89");

        ShippingDetail detail2 = new ShippingDetail();
        detail2.setAddress("Ocean View 123");
        detail2.setCountry("USA");
        detail2.setZip("89103");
        detail2.setAmount("45");

        List<ShippingDetail> answer = new ArrayList<ShippingDetail>();
        answer.add(detail);
        answer.add(detail2);
        return answer;
    }

}
