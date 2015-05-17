package camelinaction;

import org.apache.camel.Header;

/**
 * An order service bean which simulate CPU processing and may fail if the to email is FATAL
 */
public class OrderService {

    public String createMail(String order) throws Exception {
        return "Order confirmed: " + order;
    }

    public void sendMail(String body, @Header("to") String to) {
        // simulate fatal error if we refer to a special no
        if (to.equals("FATAL")) {
            throw new IllegalArgumentException("Simulated fatal error");
        }

        // simulate CPU processing of the order by sleeping a bit
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // ignore
        }
    }

}
