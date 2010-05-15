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

import org.apache.camel.Exchange;
import org.apache.camel.spi.Synchronization;
import org.apache.camel.util.FileUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A Synchronization which is to be executed when the Exchange is done.
 * <p/>
 * This allows us to execute custom logic such as rollback by deleting the file which was saved.
 *
 * @version $Revision$
 */
public class FileRollback implements Synchronization {

    private static Log LOG = LogFactory.getLog(FileRollback.class);

    public void onComplete(Exchange exchange) {
        // this method is invoked when the Exchange completed with no failure
    }

    public void onFailure(Exchange exchange) {
        // this method is executed when the Exchange failed.
        // the cause Exception is stored on the Exchange which you can obtain using getException

        // delete the file
        String name = exchange.getIn().getHeader(Exchange.FILE_NAME_PRODUCED, String.class);
        LOG.warn("Failure occurred so deleting backup file: " + name);

        FileUtil.deleteFile(new File(name));
    }

}
