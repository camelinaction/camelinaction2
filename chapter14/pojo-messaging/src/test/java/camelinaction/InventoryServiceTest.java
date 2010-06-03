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

import javax.sql.DataSource;

import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class InventoryServiceTest extends CamelSpringTestSupport {

    private static final Log LOG = LogFactory.getLog(InventoryServiceTest.class);

    private JdbcTemplate jdbc;

    @Before
    public void setup() throws Exception {
        DataSource ds = context.getRegistry().lookup("inventoryDB", DataSource.class);
        jdbc = new JdbcTemplate(ds);
    }
    
    protected int getExpectedRouteCount() {
        return 0;
    }

    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/camel-context.xml");
    }

    @Test
    public void testUpdateInventory() throws Exception {
        assertEquals(0, jdbc.queryForInt("select count(*) from partner_inventory"));
        
        // prepare an inventory update to be send
        Inventory inventory = new Inventory("1234", "4444");
        inventory.setName("Bumper");
        inventory.setAmount("57");
        
        template.sendBody("jms:partnerInventoryUpdate", inventory);

        Thread.sleep(1000);
        
        assertEquals(1, jdbc.queryForInt("select count(*) from partner_inventory"));
    }

}
