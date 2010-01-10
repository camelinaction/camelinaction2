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
 * A simple audit service which will AUDIT log incoming orders
 *
 * @version $Revision$
 */
public class AuditService {

    private Log LOG = LogFactory.getLog(AuditService.class);

    public void auditFile(String body) {
        // transform the message into pieces we can grab interesting data from
        String[] parts = body.split(",");
        String id = parts[0];
        String customerId = parts[1];

        // construct the Audit message according to requirements
        // which is often something readable by humans
        String msg = "Customer " + customerId + " send order id " + id;

        // audit log it
        LOG.info(msg);
    }

}
