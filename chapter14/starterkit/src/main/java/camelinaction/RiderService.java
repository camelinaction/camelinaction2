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

import java.util.List;

/**
 * The client interface which clients should use to communicate with Auto Rider Parts.
 * <p/>
 * This interface is used to hide the middleware and will at runtime be proxied by Camel Proxy
 * which will route the method call in Camel routes to external endpoints.
 *
 * @version $Revision$
 */
public interface RiderService {

    void updateInventory(Inventory inventory);

    List<ShippingDetail> shipInventory(String supplerId, String partId);

}
