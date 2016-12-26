package camelinaction;

import java.io.IOException;

/**
 * A stateful counter service implemented as a bean.
 * <p/>
 * Notice that this bean can store state
 * unlike the plain HystrixCommand implementation in the hystrix example.
 */
public class CounterService {

    // counter state
    private int counter;

    public String count() throws IOException {
        counter++;
        if (counter % 5 == 0) {
            throw new IOException("Forced error");
        }
        return "Count " + counter;
    }

}
