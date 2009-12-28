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

import camelinaction.order.acme.InputOrder;
import camelinaction.order.acme.OutputOrder;
import org.apache.camel.Exchange;
import org.apache.camel.component.dataset.DataSetEndpoint;
import org.apache.camel.component.dataset.DataSetSupport;
import org.apache.camel.util.ObjectHelper;
import org.junit.Assert;

/**
 * Acme DataSet is used for creating messages and asserting valid reply
 * for testing with DataSet
 *
 * @version $Revision$
 */
public class AcmeDataSet extends DataSetSupport {

    @Override
    protected Object createMessageBody(long index) {
        // create a new input object for the given index

        // we just used some fixed values but you can construct
        // these as dynamic as you like
        InputOrder input = new InputOrder();
        input.setAmount("1");
        input.setCustomerId("123");
        input.setPartId("4444" + index);

        // use index as reference no
        input.setRefNo("" + index);
        return input;
    }

    @Override
    public void assertMessageExpected(DataSetEndpoint dataSetEndpoint, Exchange expected, Exchange actual, long index) throws Exception {
        InputOrder input = expected.getIn().getBody(InputOrder.class);
        OutputOrder output = actual.getIn().getBody(OutputOrder.class);

        String refNo = ObjectHelper.after(output.getCode(), ";");

        // reference number should match so we know we got the right reply back
        Assert.assertEquals(input.getRefNo(), refNo);
    }
}
