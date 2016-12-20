package camelinaction;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test of basic hystrix command in pure Java with fallback
 */
public class MyCommandWithFallbackTest {

    @Test
    public void testSuccess() {
        // reset counter
        GlobalState.COUNTER = 0;

        String out = new MyCommandWithFallback().execute();

        Assert.assertEquals("Count 1", out);
    }

    @Test
    public void testFailureWithFallback() {
        // reset counter
        GlobalState.COUNTER = 0;

        // the first 4 should be okay
        Assert.assertEquals("Count 1", new MyCommandWithFallback().execute());
        Assert.assertEquals("Count 2", new MyCommandWithFallback().execute());
        Assert.assertEquals("Count 3", new MyCommandWithFallback().execute());
        Assert.assertEquals("Count 4", new MyCommandWithFallback().execute());

        // this should use fallback
        Assert.assertEquals("No Counter", new MyCommandWithFallback().execute());

        // and the following works again
        Assert.assertEquals("Count 6", new MyCommandWithFallback().execute());
        Assert.assertEquals("Count 7", new MyCommandWithFallback().execute());
        Assert.assertEquals("Count 8", new MyCommandWithFallback().execute());
        Assert.assertEquals("Count 9", new MyCommandWithFallback().execute());

        // this should use fallback
        Assert.assertEquals("No Counter", new MyCommandWithFallback().execute());
    }
}
