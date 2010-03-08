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

import javax.sql.DataSource;

import camelinaction.order.InputOrder;
import camelinaction.order.OutputOrder;
import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @version $Revision$
 */
public class OrderTest extends CamelSpringTestSupport {

    private JdbcTemplate jdbc;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        // setup JDBC template
        DataSource ds = context.getRegistry().lookup("myDataSource", DataSource.class);
        jdbc = new JdbcTemplate(ds);

        // notice that the table is automatic created using the OrderCreateTable class.
    }

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/camel-route.xml");
    }

    @Test
    public void testOrderOk() throws Exception {
        // there should be 0 row in the database when we start
        assertEquals(0, jdbc.queryForInt("select count(*) from riders_order"));

        InputOrder input = new InputOrder();
        input.setCustomerId("4444");
        input.setRefNo("57123");
        input.setPartId("333");
        input.setAmount("50");

        // give CXF time to wake up
        Thread.sleep(1000);

        OutputOrder reply = template.requestBody("cxf:bean:orderEndpoint", input, OutputOrder.class);
        assertEquals("OK", reply.getCode());

        // there should be 1 row in the database with the inserted order
        assertEquals(1, jdbc.queryForInt("select count(*) from riders_order"));
    }

    @Test
    public void testOrderFailOnce() throws Exception {
        // there should be 0 row in the database when we start
        assertEquals(0, jdbc.queryForInt("select count(*) from riders_order"));

        InputOrder input = new InputOrder();
        input.setCustomerId("4444");
        // by using FAIL-ONCE as ref no we simulate failure in first processing
        input.setRefNo("FAIL-ONCE");
        input.setPartId("333");
        input.setAmount("50");

        // give CXF time to wake up
        Thread.sleep(1000);

        OutputOrder reply = template.requestBody("cxf:bean:orderEndpoint", input, OutputOrder.class);
        assertEquals("OK", reply.getCode());

        // there should be 1 row in the database with the inserted order
        assertEquals(1, jdbc.queryForInt("select count(*) from riders_order"));
    }

    @Test
    public void testOrderFailAll() throws Exception {
        // there should be 0 row in the database when we start
        assertEquals(0, jdbc.queryForInt("select count(*) from riders_order"));

        InputOrder input = new InputOrder();
        input.setCustomerId("4444");
        // by using FATAL as ref no we simulate failure in all processing
        input.setRefNo("FATAL");
        input.setPartId("333");
        input.setAmount("50");

        // give CXF time to wake up
        Thread.sleep(1000);

        OutputOrder reply = template.requestBody("cxf:bean:orderEndpoint", input, OutputOrder.class);
        assertEquals("ERROR: Simulated fatal error", reply.getCode());

        // there should still be 0 row in the database as the entire route was rolled back
        assertEquals(0, jdbc.queryForInt("select count(*) from riders_order"));
    }

}