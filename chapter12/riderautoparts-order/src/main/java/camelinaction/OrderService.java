package camelinaction;

import java.io.IOException;

import camelinaction.order.InputOrder;
import camelinaction.order.OutputOrder;
import org.apache.camel.Exchange;
import org.apache.camel.Header;

public class OrderService {

    public void processOrder(Exchange exchange, InputOrder order,
                             @Header(Exchange.REDELIVERED) Boolean redelivered) throws Exception {

        // simulate CPU processing of the order by sleeping a bit
        Thread.sleep(1000);

        // simulate fatal error if we refer to a special no
        if (order.getRefNo().equals("FATAL")) {
            throw new IllegalArgumentException("Simulated fatal error");
        }

        // simulate fail once if we have not yet redelivered, which means its the first
        // time processOrder method is called
        if (order.getRefNo().equals("FAIL-ONCE") && redelivered == null) {
            throw new IOException("Simulated failing once");
        }

        // processing is okay
    }

    public OutputOrder replyOk() {
        OutputOrder ok = new OutputOrder();
        ok.setCode("OK");
        return ok;
    }

    public OutputOrder replyError(Exception cause) {
        OutputOrder error = new OutputOrder();
        error.setCode("ERROR: " + cause.getMessage());
        return error;
    }


}
