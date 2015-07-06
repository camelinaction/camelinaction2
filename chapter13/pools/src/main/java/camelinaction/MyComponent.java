package camelinaction;

import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A custom component using {@link org.apache.camel.spi.ExecutorServiceManager} to create a thread pool.
 *
 * @version $Revision$
 */
public class MyComponent extends DefaultComponent implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(MyComponent.class);
    private ScheduledExecutorService executor;

    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        return null;
    }

    public void run() {
        // this is the task being executed every second
        LOG.info("I run now");
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        // create a scheduled thread pool with 1 thread as we only need one task as background task
        executor = getCamelContext().getExecutorServiceManager().newScheduledThreadPool(this, "MyBackgroundTask", 1);
        // schedule the task to run once every second
        executor.scheduleWithFixedDelay(this, 1, 1, TimeUnit.SECONDS);
    }

    @Override
    protected void doStop() throws Exception {
        // shutdown the thread pool
        getCamelContext().getExecutorServiceManager().shutdown(executor);
        super.doStop();
    }

}
