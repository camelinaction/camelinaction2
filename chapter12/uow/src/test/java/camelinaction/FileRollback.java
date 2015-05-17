package camelinaction;

import java.io.File;

import org.apache.camel.Exchange;
import org.apache.camel.spi.Synchronization;
import org.apache.camel.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Synchronization which is to be executed when the Exchange is done.
 * <p/>
 * This allows us to execute custom logic such as rollback by deleting the file which was saved.
 */
public class FileRollback implements Synchronization {

    private static Logger LOG = LoggerFactory.getLogger(FileRollback.class);

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
