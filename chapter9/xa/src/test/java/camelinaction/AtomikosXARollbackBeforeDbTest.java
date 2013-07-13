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

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @version $Revision$
 */
public class AtomikosXARollbackBeforeDbTest extends CamelSpringTestSupport {

    private JdbcTemplate jdbc;

    @Before
    public void setupDatabase() throws Exception {
        DataSource ds = context.getRegistry().lookup("myDataSource", DataSource.class);
        jdbc = new JdbcTemplate(ds);

        jdbc.execute("create table partner_metric "
            + "( partner_id varchar(10), time_occurred varchar(20), status_code varchar(3), perf_time varchar(10) )");
    }

    @After
    public void dropDatabase() throws Exception {
        jdbc.execute("drop table partner_metric");
    }

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("spring-context.xml");
    }

    @Test
    public void testXaRollbackBeforeDb() throws Exception {
        // there should be 0 row in the database when we start
        assertEquals(0, jdbc.queryForInt("select count(*) from partner_metric"));

        // partner id as 0 will cause rollback
        String xml = "<?xml version=\"1.0\"?><partner id=\"0\"><date>200911150927</date><code>500</code><time>8732</time></partner>";
        template.sendBody("activemq:queue:partners", xml);

        // wait for the route to complete with failure
        Thread.sleep(15000);

        // data not inserted so there should be 0 rows
        assertEquals(0, jdbc.queryForInt("select count(*) from partner_metric"));

        // should be in DLQ
        // now check that the message is on the queue by consuming it again
        String dlq = consumer.receiveBodyNoWait("activemq:queue:ActiveMQ.DLQ", String.class);
        assertNotNull("Should not lose message", dlq);
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("activemq:queue:partners")
                    .transacted()
                    .log("*** before database ***")
                    .bean(PartnerServiceBean.class, "toSql")
                    .to("jdbc:myDataSource?resetAutoCommit=false") // the usage of the resetAutoCommit option (available since 2.9.0) has the side effect of JDBC commit
                    .log("*** after database ***")                 // not being called through JdbcProducer (We need this as we make use of global transaction boundries)
                    .throwException(new IllegalArgumentException("Forced failure after DB"));
            }
        };
    }

}
