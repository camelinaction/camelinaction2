package camelinaction;

import java.util.concurrent.ExecutorService;

import org.apache.camel.AsyncCallback;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultAsyncProducer;

/**
 * Producer to simulate asynchronous communication with ERP system.
 */
public class ErpProducer extends DefaultAsyncProducer {

    // use a thread pool for async communication with ERP
    private ExecutorService executor;

    public ErpProducer(Endpoint endpoint) {
        super(endpoint);
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        // use Camel to create the thread pool for us
        this.executor = getEndpoint().getCamelContext().getExecutorServiceManager().newFixedThreadPool(this, "ERP", 10);
    }

    @Override
    protected void doStop() throws Exception {
        super.doStop();
        // shutdown thread pool when we stop
        getEndpoint().getCamelContext().getExecutorServiceManager().shutdown(executor);
    }

    public boolean process(final Exchange exchange, final AsyncCallback callback) {
        // simulate async communication using a thread pool in which will return a reply in 5 seconds.
        executor.submit(new ERPTask(exchange, callback));

        // return false to tell Camel that we process asynchronously
        // which enables the Camel routing engine to know this and act accordingly
        // notice the ERPTask must invoke the callback.done(false) because what
        // we return here must match the boolean in the callback.done method.
        log.info("Returning false (processing will continue asynchronously)");
        return false;
    }

    private class ERPTask implements Runnable {

        private final Exchange exchange;
        private final AsyncCallback callback;

        private ERPTask(Exchange exchange, AsyncCallback callback) {
            this.exchange = exchange;
            this.callback = callback;
        }

        public void run() {
            log.info("Calling ERP");
            // simulate communication with ERP takes 5 seconds
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // ignore
            }
            log.info("ERP reply received");

            // set reply
            String in = exchange.getIn().getBody(String.class);
            exchange.getOut().setBody(in + ";516");

            // notify callback we are done
            // we must use done(false) because the process method returned false
            log.info("Continue routing");
            callback.done(false);
        }
    }
}
