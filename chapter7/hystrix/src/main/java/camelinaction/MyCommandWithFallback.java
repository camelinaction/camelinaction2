package camelinaction;

import java.io.IOException;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

import static camelinaction.GlobalState.COUNTER;

/**
 * Our custom command that is wrapped in Hystrix with fallback functionality
 */
public class MyCommandWithFallback extends HystrixCommand<String> {

    public MyCommandWithFallback() {
        super(HystrixCommandGroupKey.Factory.asKey("MyGroup"));
    }

    @Override
    protected String run() throws Exception {
        COUNTER++;
        if (COUNTER % 5 == 0) {
            throw new IOException("Forced error");
        }
        return "Count " + COUNTER;
    }

    @Override
    protected String getFallback() {
        // when the run command fails then use this as fallback
        return "No Counter";
    }
}
