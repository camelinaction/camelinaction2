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

import java.io.File;
import java.net.ConnectException;
import javax.sql.DataSource;

import camelinaction.order.acme.InputOrder;
import camelinaction.order.acme.OutputOrder;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.apache.camel.util.FileUtil;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @version $Revision$
 */
public class AcmeOrderTest extends CamelSpringTestSupport {

    private static int counter;
    private JdbcTemplate jdbc;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        DataSource ds = context.getRegistry().lookup("myDataSource", DataSource.class);
        jdbc = new JdbcTemplate(ds);

        jdbc.execute("create table acme_order "
            + "( customer_id varchar(10), ref_no varchar(10), part_id varchar(10), amount varchar(10) )");
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        jdbc.execute("drop table acme_order");
    }

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/camel-route.xml");
    }

    @Test
    public void testAcmeOrderOk() throws Exception {
        // clean output folder
        deleteDirectory(FtpServerBean.FTP_ROOT_DIR + "rider");

        // there should be 0 row in the database when we start
        assertEquals(0, jdbc.queryForInt("select count(*) from acme_order"));

        InputOrder input = new InputOrder();
        input.setCustomerId("4444");
        input.setRefNo("57123");
        input.setPartId("333");
        input.setAmount("50");

        OutputOrder reply = template.requestBody("cxf:bean:acmeOrderEndpoint", input, OutputOrder.class);
        assertEquals("OK", reply.getCode());

        // test the file exists
        File file = new File(FileUtil.normalizePath(FtpServerBean.FTP_ROOT_DIR + "rider/57123.csv"));
        assertTrue("File should exist", file.exists());

        // and check that its content is correct
        String body = context.getTypeConverter().convertTo(String.class, file);
        assertEquals("4444,333,50=OK", body);

        // there should be 1 row in the database with the inserted order
        assertEquals(1, jdbc.queryForInt("select count(*) from acme_order"));
    }

    @Test
    public void testAcmeOrderFirstFtpUploadFail() throws Exception {
        counter = 0;

        // simulate that the first upload to FTP failed but we should succeed on 2nd attempt
        context.getRouteDefinitions().get(0).adviceWith(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                interceptSendToEndpoint("ftp:*").process(new Processor() {
                    public void process(Exchange exchange) throws Exception {
                        if (counter ++ == 0) {
                            throw new ConnectException("Simulated error");
                        }
                    }
                });
            }
        });

        // give CXF time to adjust to advice
        Thread.sleep(5000);

        // clean output folder
        deleteDirectory(FtpServerBean.FTP_ROOT_DIR + "rider");

        // there should be 0 row in the database when we start
        assertEquals(0, jdbc.queryForInt("select count(*) from acme_order"));

        InputOrder input = new InputOrder();
        input.setCustomerId("4444");
        input.setRefNo("57123");
        input.setPartId("333");
        input.setAmount("50");

        OutputOrder reply = template.requestBody("cxf:bean:acmeOrderEndpoint", input, OutputOrder.class);
        assertEquals("OK", reply.getCode());

        // test the file exists
        File file = new File(FileUtil.normalizePath(FtpServerBean.FTP_ROOT_DIR + "rider/57123.csv"));
        assertTrue("File should exist", file.exists());

        // and check that its content is correct
        String body = context.getTypeConverter().convertTo(String.class, file);
        assertEquals("4444,333,50=OK", body);

        // there should be 1 row in the database with the inserted order
        assertEquals(1, jdbc.queryForInt("select count(*) from acme_order"));
    }

    @Test
    public void testAcmeOrderFtpUploadFail() throws Exception {
        // simulate that all FTP upload fails
        context.getRouteDefinitions().get(0).adviceWith(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                interceptSendToEndpoint("ftp:*")
                    .skipSendToOriginalEndpoint()
                    .throwException(new ConnectException("Simulated error"));
            }
        });

        // give CXF time to adjust to advice
        Thread.sleep(5000);

        // clean output folder
        deleteDirectory(FtpServerBean.FTP_ROOT_DIR + "rider");

        // there should be 0 row in the database when we start
        assertEquals(0, jdbc.queryForInt("select count(*) from acme_order"));

        InputOrder input = new InputOrder();
        input.setCustomerId("4444");
        input.setRefNo("57123");
        input.setPartId("333");
        input.setAmount("50");

        OutputOrder reply = template.requestBody("cxf:bean:acmeOrderEndpoint", input, OutputOrder.class);
        assertEquals("ERROR: Simulated error", reply.getCode());

        // test the file does NOT exist
        File file = new File(FtpServerBean.FTP_ROOT_DIR + "rider/57123.csv");
        assertFalse("File should NOT exist", file.exists());

        // there should still be 0 row in the database as the entire route was rolled back
        assertEquals(0, jdbc.queryForInt("select count(*) from acme_order"));
    }

}