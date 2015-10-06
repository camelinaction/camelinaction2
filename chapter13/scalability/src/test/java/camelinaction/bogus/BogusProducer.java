package camelinaction.bogus;

import java.util.concurrent.ExecutorService;

import org.apache.camel.AsyncCallback;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultAsyncProducer;

public class BogusProducer extends DefaultAsyncProducer {

    // use a thread pool for async communication
    private ExecutorService executor;

    public BogusProducer(Endpoint endpoint) {
        super(endpoint);
    }

    @Override
    public boolean process(final Exchange exchange, final AsyncCallback callback) {
        // validate that the message has some data
        try {
            String body = exchange.getIn().getBody(String.class);
            if (body == null || body.contains("Donkey")) {
                throw new IllegalArgumentException("Real developers ride Camels");
            }
        } catch (Exception e) {
            // idiom to catch exception and set on Exchange
            exchange.setException(e);
            // and need to call callback before returning when using true
            callback.done(true);
            // return true
            return true;
        }

        // the rest runs in asynchronous mode, so we return false
        BogusTask task = new BogusTask(exchange, callback);
        executor.submit(task);
        return false;
    }

    private class BogusTask implements Runnable {

        private final Exchange exchange;
        private final AsyncCallback callback;

        private BogusTask(Exchange exchange, AsyncCallback callback) {
            this.exchange = exchange;
            this.callback = callback;
        }

        @Override
        public void run() {
            boolean block = "ActiveMQ in Action".equals(exchange.getIn().getBody());

            log.info("Calling ERP");
            // simulate communication with ERP takes 5 seconds by default and 2 minutes if being blocked
            try {
                int delay = block ? 2 * 60 * 1000 : 5000;
                Thread.sleep(delay);
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
}
