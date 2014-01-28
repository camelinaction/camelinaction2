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

/**
 * Customer service bean
 *
 * @version $Revision$
 */
public class CustomerService {

    /**
     * Split the customer into a list of departments
     *
     * @param customer the customer
     * @return the departments
     */
    public List<Department> splitDepartments(Customer customer) {
        // this is a very simple logic, but your use cases
        // may very well require more complex logic
        return customer.getDepartments();
    }

    /**
     * Create a dummy customre for testing purprose
     */
    public static Customer createCustomer() {
        List<Department> departments = new ArrayList<Department>();
        departments.add(new Department(222, "Oceanview 66", "89210", "USA"));
        departments.add(new Department(333, "Lakeside 41", "22020", "USA"));
        departments.add(new Department(444, "Highstreet 341", "11030", "USA"));

        Customer customer = new Customer(123, "Honda", departments);
        return customer;
    }
}
