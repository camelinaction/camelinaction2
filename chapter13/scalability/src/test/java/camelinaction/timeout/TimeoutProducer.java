package camelinaction.timeout;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.camel.AsyncCallback;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangeTimedOutException;
import org.apache.camel.impl.DefaultAsyncProducer;

public class TimeoutProducer extends DefaultAsyncProducer {

    // use a thread pool for async communication
    private ExecutorService executor;
    private final long timeout;

    public TimeoutProducer(Endpoint endpoint, long timeout) {
        super(endpoint);
        this.timeout = timeout;
    }

    @Override
    public boolean process(final Exchange exchange, final AsyncCallback callback) {
        CallRemoteSystemTask task = new CallRemoteSystemTask(exchange, callback, timeout);
        executor.submit(task);
        return false;
    }

    private class CallRemoteSystemTask implements Runnable {

        private final Exchange exchange;
        private final AsyncCallback callback;
        private final long timeout;

        private CallRemoteSystemTask(Exchange exchange, AsyncCallback callback, long timeout) {
            this.exchange = exchange;
            this.callback = callback;
            this.timeout = timeout;
        }

        @Override
        public void run() {
            try {
                String in = exchange.getIn().getBody(String.class);

                // create remote task that simulates calling remote system
                Future<String> future = executor.submit(new ERPTask(in));

                // get the response from the future, with timeout support
                String response = future.get(timeout, TimeUnit.MILLISECONDS);

                // set response on body
                if (response != null) {
                    exchange.getOut().setBody(in + ";" + response);
                }
            } catch (TimeoutException e) {
                // timeout happened, so set this as a ExchangeTimedOutException so its a specialized timeout exception
                exchange.setException(new ExchangeTimedOutException(exchange, timeout, "Timeout waiting for reply from ERP system"));
            } catch (Exception e) {
                // in case of any other exception set that on the exchange
                exchange.setException(e);
            } finally {
                // always notify callback we are done (so we do this in finally block)
                // we must use done(false) because the process method returned false
                log.info("Continue routing");
                callback.done(false);
            }
        }
    }

    /**
     * Task that simulate calling the remote system with timeout support
     * <p/>
     * Instead of using a 3rd party client to do a remote network call, we
     * reuse the thread pool to submit a task, which we can use a Future to detect if there was a timeout or not.
     * <p/>
     * Ideally the 3rd party clients should offer timeout out of the box, and you should just leverage this, and
     * use the timeout configuration from the Camel endpoint as configuration value.
     */
    private class ERPTask implements Callable<String> {

        private final String request;

        private ERPTask(String request) {
            this.request = request;
        }

        @Override
        public String call() throws Exception {
            // if we order AMQ in Action the system takes too long time to response
            boolean block = "ActiveMQ in Action".equals(request);

            log.info("Calling ERP with timeout {} sec.", timeout);
            // simulate communication with ERP takes 5 seconds
            try {
                int delay = block ? 10 * 60 * 1000 : 5000;
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                // ignore
            }
            log.info("ERP reply received");
            return "516";
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
