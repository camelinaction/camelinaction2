package camelinaction;

import java.io.IOException;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

import static camelinaction.GlobalState.COUNTER;

/**
 * Our custom command that is wrapped in Hystrix
 */
public class MyCommand extends HystrixCommand<String> {

    public MyCommand() {
        super(HystrixCommandGroupKey.Factory.asKey("MyGroup"));
    }

    @Override
    protected String run() throws Exception {
        // use a shared state as counter
        // as you cannot store any state in HystrixCommand
        COUNTER++;
        if (COUNTER % 5 == 0) {
            throw new IOException("Forced error");
        }
        return "Count " + COUNTER;
    }
}
